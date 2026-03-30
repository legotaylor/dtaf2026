/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.mixin.entity;

import dev.dannytaylor.dtaf2026.common.registry.effect.StatusEffectRegistry;
import dev.dannytaylor.dtaf2026.common.registry.entity.AttributeModifierRegistry;
import dev.dannytaylor.dtaf2026.common.registry.entity.SomniumRealeLivingEntity;
import dev.dannytaylor.dtaf2026.common.registry.worldgen.DimensionRegistry;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	@Unique private static final TrackedData<Optional<BlockPos>> lastBedPos;
	@Shadow public abstract void tick();
	@Shadow private int sleepTimer;

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "initDataTracker", at = @At("RETURN"))
	private void dtaf2026$initDataTracker(DataTracker.Builder builder, CallbackInfo ci) {
		builder.add(lastBedPos, Optional.empty());
	}

	@Inject(method = "tick", at = @At("RETURN"))
	private void dtaf2026$tick(CallbackInfo ci) {
		if (this.isSleeping() && this.sleepTimer == 100) {
			RegistryKey<World> currentWorld = this.getWorld().getRegistryKey();
			TeleportTarget teleportTarget = null;
			System.out.println(currentWorld.getValue());
			if (currentWorld.equals(World.OVERWORLD)) {
				teleportTarget = dtaf2026$createTeleportTarget(DimensionRegistry.somniumReale.world(), false);
			} else if (currentWorld.equals(DimensionRegistry.somniumReale.world()) || currentWorld.equals(DimensionRegistry.theTerrorlands.world())) {
				teleportTarget = dtaf2026$createTeleportTarget(World.OVERWORLD, true);
			}
			if (teleportTarget != null) {
				if (currentWorld.equals(World.OVERWORLD) || currentWorld.equals(DimensionRegistry.somniumReale.world()) || currentWorld.equals(DimensionRegistry.theTerrorlands.world())) {
					// Reset block before teleporting.
					this.getSleepingPosition().filter((blockPos) -> {
						ChunkPos chunkPos = this.getWorld().getChunk(blockPos).getPos();
						return this.getWorld().isChunkLoaded(chunkPos.x, chunkPos.z);
					}).ifPresent((blockPos) -> {
						BlockState blockState = this.getWorld().getBlockState(blockPos);
						if (blockState.getBlock() instanceof BedBlock) this.getWorld().setBlockState(blockPos, blockState.with(BedBlock.OCCUPIED, false), 3);
					});
					this.setPose(EntityPose.STANDING);
					this.clearSleepingPosition();
					// Teleport to dimension once fully asleep.
					if (currentWorld.equals(World.OVERWORLD)) dtaf2026$setLastBedPos(this.getBlockPos());
					this.teleportTo(teleportTarget);
				}
			}
		}
		this.dtaf2026$updateSomniumReale();
	}

	@Unique
	private void dtaf2026$setLastBedPos(BlockPos blockPos) {
		this.dataTracker.set(lastBedPos, Optional.of(blockPos));
	}

	@Unique
	private void dtaf2026$clearLastBedPos() {
		this.dataTracker.set(lastBedPos, Optional.empty());
	}

	@Override
	public void setHealth(float health) {
		// Peaceful, Easy, Normal difficulties will prevent death in the Somnium Reale dimension.
		// You can die on Hard difficulty in the Somnium Reale dimension.
		if ((this.getWorld().getRegistryKey() == DimensionRegistry.somniumReale.world() || this.getWorld().getRegistryKey() == DimensionRegistry.theTerrorlands.world()) && this.getWorld().getDifficulty().ordinal() <= 2 && health < 1.0F) {
			this.setOnFire(false); // We don't want the player dying once they teleport either.
			this.setFrozenTicks(0);
			this.setVelocity(Vec3d.ZERO);
			this.setHealth(1.0F);
			TeleportTarget teleportTarget = dtaf2026$createTeleportTarget(World.OVERWORLD, true);
			if (teleportTarget != null) this.teleportTo(teleportTarget);
		} else super.setHealth(health);
	}

	@Unique
	private TeleportTarget dtaf2026$createTeleportTarget(RegistryKey<World> registryKey, boolean toRespawnPos) {
		if (this.getServer() != null) {
			ServerWorld serverWorld = this.getServer().getWorld(registryKey);
			if (serverWorld != null) {
				BlockPos blockPos = serverWorld.getSpawnPos();
				if (toRespawnPos) {
					Optional<BlockPos> lastBedPos = this.dataTracker.get(PlayerEntityMixin.lastBedPos);
					if (lastBedPos.isPresent()) {
						Optional<Vec3d> wakeUpPos = BedBlock.findWakeUpPosition(this.getType(), serverWorld, lastBedPos.get(), serverWorld.getBlockState(lastBedPos.get()).get(BedBlock.FACING), serverWorld.getSpawnAngle());
						blockPos = wakeUpPos.isPresent() ? BlockPos.ofFloored(wakeUpPos.get()) : DimensionRegistry.toHighestBlockPos(serverWorld, blockPos);
					}
				} else {
					blockPos = DimensionRegistry.toHighestBlockPos(serverWorld, blockPos);
				}
				return new TeleportTarget(serverWorld, blockPos.toBottomCenterPos(), Vec3d.ZERO, serverWorld.getSpawnAngle(), 0.0F, PositionFlag.combine(PositionFlag.DELTA, PositionFlag.ROT), TeleportTarget.SEND_TRAVEL_THROUGH_PORTAL_PACKET.then(TeleportTarget.ADD_PORTAL_CHUNK_TICKET));
			}
		}
		return null;
	}

	@Unique
	private void dtaf2026$updateSomniumReale() {
		((SomniumRealeLivingEntity)this).dtaf2026$updateAttribute(EntityAttributes.SCALE, AttributeModifierRegistry.somniumRealeScale, () -> ((SomniumRealeLivingEntity)this).dtaf2026$isInAbstractSomniumReale());
	}

	@ModifyVariable(method = "applyEnchantmentCosts", at = @At("HEAD"), index = 2, argsOnly = true)
	public int dtaf2026$applyEnchantmentCosts(int value) {
		return this.hasStatusEffect(StatusEffectRegistry.enchanted) ? 0 : value;
	}

	static {
		lastBedPos = DataTracker.registerData(PlayerEntityMixin.class, TrackedDataHandlerRegistry.OPTIONAL_BLOCK_POS);
	}
}
