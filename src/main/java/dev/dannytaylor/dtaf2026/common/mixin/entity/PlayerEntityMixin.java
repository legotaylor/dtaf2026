/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.mixin.entity;

import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.*;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.EndPlatformFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	@Shadow
	public abstract void tick();

	@Shadow
	private int sleepTimer;

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "tick", at = @At("RETURN"))
	private void dtaf2026$tick(CallbackInfo ci) {
		if (this.isSleeping() && this.sleepTimer >= 100) {
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
			this.teleportTo(createTeleportTarget(World.END, (PlayerEntity) (Object) this));
		}
	}

	@Unique
	private TeleportTarget createTeleportTarget(RegistryKey<World> registryKey, Entity entity) {
		if (this.getServer() != null) {
			ServerWorld serverWorld = this.getServer().getWorld(registryKey);
			if (serverWorld != null) {
				boolean bl = registryKey == World.END;
				BlockPos blockPos = bl ? ServerWorld.END_SPAWN_POS : serverWorld.getSpawnPos();
				Vec3d vec3d = blockPos.toBottomCenterPos();
				float f;
				Set<PositionFlag> set;
				if (bl) {
					EndPlatformFeature.generate(serverWorld, BlockPos.ofFloored(vec3d).down(), true);
					f = Direction.WEST.getPositiveHorizontalDegrees();
					set = PositionFlag.combine(PositionFlag.DELTA, Set.of(PositionFlag.X_ROT));
					if (entity instanceof ServerPlayerEntity) {
						vec3d = vec3d.subtract(0.0F, 1.0F, 0.0F);
					}
				} else {
					f = serverWorld.getSpawnAngle();
					set = PositionFlag.combine(PositionFlag.DELTA, PositionFlag.ROT);
					if (entity instanceof ServerPlayerEntity serverPlayerEntity) {
						return serverPlayerEntity.getRespawnTarget(false, TeleportTarget.NO_OP);
					}

					vec3d = entity.getWorldSpawnPos(serverWorld, blockPos).toBottomCenterPos();
				}

				return new TeleportTarget(serverWorld, vec3d, Vec3d.ZERO, f, 0.0F, set, TeleportTarget.SEND_TRAVEL_THROUGH_PORTAL_PACKET.then(TeleportTarget.ADD_PORTAL_CHUNK_TICKET));
			}
		}
		return null;
	}
}
