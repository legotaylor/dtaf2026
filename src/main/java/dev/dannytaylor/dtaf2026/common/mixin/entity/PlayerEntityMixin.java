/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.mixin.entity;

import dev.dannytaylor.dtaf2026.common.registry.AttributeModifierRegistry;
import dev.dannytaylor.dtaf2026.common.registry.DimensionRegistry;
import dev.dannytaylor.dtaf2026.common.registry.TagRegistry;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
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
		RegistryKey<World> currentWorld = this.getWorld().getRegistryKey();
		TeleportTarget teleportTarget = null;
		if (currentWorld.equals(World.OVERWORLD)) {
			teleportTarget = createTeleportTarget(DimensionRegistry.somniumReale.world(), false);
		} else if (currentWorld.equals(DimensionRegistry.somniumReale.world())) {
			teleportTarget = createTeleportTarget(World.OVERWORLD, true);
		}
		if (teleportTarget != null) {
			if (this.isSleeping() && this.sleepTimer >= 100) {
				if (currentWorld.equals(World.OVERWORLD) || currentWorld.equals(DimensionRegistry.somniumReale.world())) {
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
					if (currentWorld.equals(World.OVERWORLD)) setLastBedPos(this.getBlockPos());
					this.teleportTo(teleportTarget);
				}
			} else {
				if (!this.isSleeping() && !currentWorld.equals(DimensionRegistry.somniumReale.world())) clearLastBedPos();
			}
		}
		this.updateSomniumRealeBiome();
	}

	@Unique
	private void setLastBedPos(BlockPos blockPos) {
		this.dataTracker.set(lastBedPos, Optional.of(blockPos));
	}

	@Unique
	private void clearLastBedPos() {
		this.dataTracker.set(lastBedPos, Optional.empty());
	}

	@Inject(method = "applyDamage", at = @At("HEAD"), cancellable = true)
	protected void dtaf2026$applyDamage(ServerWorld world, DamageSource source, float amount, CallbackInfo ci) {
		// Peaceful, Easy, Normal difficulties will prevent death in the Somnium Reale dimension.
		// You can die on Hard difficulty in the Somnium Reale dimension.
		if (world.getDifficulty().ordinal() <= 2) {
			TeleportTarget teleportTarget = createTeleportTarget(World.OVERWORLD, true);
			if (teleportTarget != null) {
				if (world.getRegistryKey() == DimensionRegistry.somniumReale.world()) {
					if (this.getHealth() - amount < 1.0F) {
						this.setVelocity(Vec3d.ZERO);
						this.teleportTo(teleportTarget);
						this.setHealth(1.0F);
						ci.cancel();
					}
				}
			}
		}
	}

	@Unique
	private TeleportTarget createTeleportTarget(RegistryKey<World> registryKey, boolean toRespawnPos) {
		if (!this.getWorld().isClient && this.getServer() != null) {
			ServerWorld serverWorld = this.getServer().getWorld(registryKey);
			if (serverWorld != null) {
				BlockPos blockPos = serverWorld.getSpawnPos();
				if (toRespawnPos) {
					Optional<BlockPos> lastBedPos = this.dataTracker.get(PlayerEntityMixin.lastBedPos);
					if (lastBedPos.isPresent()) {
						Optional<Vec3d> wakeUpPos = BedBlock.findWakeUpPosition(this.getType(), serverWorld, lastBedPos.get(), serverWorld.getBlockState(lastBedPos.get()).get(BedBlock.FACING), serverWorld.getSpawnAngle());
						if (wakeUpPos.isPresent()) {
							blockPos = BlockPos.ofFloored(wakeUpPos.get());
						}
					}
				}
				return new TeleportTarget(serverWorld, blockPos.toBottomCenterPos(), Vec3d.ZERO, serverWorld.getSpawnAngle(), 0.0F, PositionFlag.combine(PositionFlag.DELTA, PositionFlag.ROT), TeleportTarget.SEND_TRAVEL_THROUGH_PORTAL_PACKET.then(TeleportTarget.ADD_PORTAL_CHUNK_TICKET));
			}
		}
		return null;
	}

	@Unique
	private void updateSomniumRealeBiome() {
		EntityAttributeInstance entityAttributeInstance = this.getAttributeInstance(EntityAttributes.SCALE);
		if (entityAttributeInstance != null) {
			entityAttributeInstance.removeModifier(AttributeModifierRegistry.somniumRealeBiomeModifier.id());
			if (TagRegistry.WorldGen.Biome.isIn(this.getWorld(), this.getBlockPos(), TagRegistry.WorldGen.Biome.somnium_reale)) entityAttributeInstance.addTemporaryModifier(AttributeModifierRegistry.somniumRealeBiomeModifier);
		}
	}

	static {
		lastBedPos = DataTracker.registerData(PlayerEntityMixin.class, TrackedDataHandlerRegistry.OPTIONAL_BLOCK_POS);
	}
}
