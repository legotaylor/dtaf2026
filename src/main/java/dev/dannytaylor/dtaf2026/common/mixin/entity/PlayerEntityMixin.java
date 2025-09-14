/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.mixin.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.EndPlatformFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Set;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public void sleep(BlockPos pos) {
		if (this.hasVehicle()) this.stopRiding();
		this.setVelocity(Vec3d.ZERO);
		this.velocityDirty = true;
		this.teleportTo(createTeleportTarget(World.END, (PlayerEntity) (Object) this));
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
