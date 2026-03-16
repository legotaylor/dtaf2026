/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.entity.fleecifer;

import dev.dannytaylor.dtaf2026.common.registry.EntityRegistry;
import dev.dannytaylor.dtaf2026.common.registry.ItemRegistry;
import dev.dannytaylor.dtaf2026.common.registry.NetworkingRegistry;
import dev.dannytaylor.dtaf2026.common.registry.SoundEventRegistry;
import dev.dannytaylor.dtaf2026.common.registry.networking.Events;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class FleeciferEyeEntity extends Entity implements FlyingItemEntity {
	private static final TrackedData<ItemStack> ITEM = DataTracker.registerData(FleeciferEyeEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
	private int lifespan;
	@Nullable
	private Vec3d targetPos;

	public FleeciferEyeEntity(EntityType<? extends FleeciferEyeEntity> entityType, World world) {
		super(entityType, world);
	}

	public static FleeciferEyeEntity create(World world, double x, double y, double z) {
		FleeciferEyeEntity fleeciferEye = new FleeciferEyeEntity(EntityRegistry.fleeciferEye, world);
		fleeciferEye.setPosition(x, y, z);
		return fleeciferEye;
	}

	private static Vec3d updateVelocity(Vec3d velocity, Vec3d currentPos, Vec3d targetPos) {
		Vec3d vec3d = new Vec3d(targetPos.x - currentPos.x, 0.0, targetPos.z - currentPos.z);
		double d = vec3d.length();
		double e = MathHelper.lerp(0.0025, velocity.horizontalLength(), d);
		double f = velocity.y;
		if (d < 1.0) {
			e *= 0.8;
			f *= 0.8;
		}

		double g = currentPos.y - velocity.y < targetPos.y ? 1.0 : -1.0;
		return vec3d.multiply(e / d).add(0.0, f + (g - f) * 0.015, 0.0);
	}

	@Override
	public ItemStack getStack() {
		return this.getDataTracker().get(ITEM);
	}

	@Override
	protected void initDataTracker(DataTracker.Builder builder) {
		builder.add(ITEM, this.getItem());
	}

	@Override
	public boolean shouldRender(double distance) {
		if (this.age < 2 && distance < 12.25) {
			return false;
		} else {
			double d = this.getBoundingBox().getAverageSideLength() * 4.0;
			if (Double.isNaN(d)) {
				d = 4.0;
			}

			d *= 64.0;
			return distance < d * d;
		}
	}

	@Override
	public void tick() {
		super.tick();
		Vec3d vec3d = this.getPos().add(this.getVelocity());
		if (!this.getWorld().isClient() && this.targetPos != null) {
			this.setVelocity(updateVelocity(this.getVelocity(), vec3d, this.targetPos));
		}

		if (this.getWorld().isClient()) {
			Vec3d vec3d2 = vec3d.subtract(this.getVelocity().multiply(0.25));
			this.addParticles(vec3d2, this.getVelocity());
		}

		this.setPosition(vec3d);
		if (!this.getWorld().isClient()) {
			this.lifespan++;
			if (this.lifespan > 80 && !this.getWorld().isClient) {
				this.playSound(SoundEventRegistry.fleeciferEyeBreak, 1.0F, 1.0F);
				this.discard();
				this.sendPacket(this, this.getServer() != null ? this.getServer().getPlayerManager() : null, this.getWorld(), this.getBlockPos());
				FleeciferFight.createFleecifer((ServerWorld) this.getWorld(), this.getBlockPos());
			}
		}
	}

	private void sendPacket(@Nullable Entity source, PlayerManager playerManager, World world, BlockPos pos) {
		if (playerManager != null) {
			NetworkingRegistry.sendWorldEvent(
				NetworkingRegistry.getAround(playerManager, source instanceof PlayerEntity playerEntity ? playerEntity : null, pos.getX(), pos.getY(), pos.getZ(), 64.0, world.getRegistryKey()),
				Events.fleeciferEyeBreak,
				pos,
				0,
				false
			);
		}
	}

	private void addParticles(Vec3d pos, Vec3d velocity) {
		if (this.isTouchingWater()) {
			for (int i = 0; i < 4; i++) {
				this.getWorld().addParticleClient(ParticleTypes.BUBBLE, pos.x, pos.y, pos.z, velocity.x, velocity.y, velocity.z);
			}
		} else {
			this.getWorld().addParticleClient(ParticleTypes.PORTAL, pos.x + this.random.nextDouble() * 0.6 - 0.3, pos.y - 0.5, pos.z + this.random.nextDouble() * 0.6 - 0.3, velocity.x, velocity.y, velocity.z);
		}
	}

	@Override
	protected void writeCustomData(WriteView view) {
		view.put("Item", ItemStack.CODEC, this.getStack());
	}

	@Override
	protected void readCustomData(ReadView view) {
		this.setItem(view.read("Item", ItemStack.CODEC).orElse(this.getItem()));
	}

	private ItemStack getItem() {
		return new ItemStack(ItemRegistry.eyeOfFleecifer);
	}

	public void setItem(ItemStack stack) {
		if (stack.isEmpty()) {
			this.getDataTracker().set(ITEM, this.getItem());
		} else {
			this.getDataTracker().set(ITEM, stack.copyWithCount(1));
		}
	}

	@Override
	public boolean isAttackable() {
		return false;
	}

	@Override
	public boolean damage(ServerWorld world, DamageSource source, float amount) {
		return false;
	}

	public void initTargetPos(Vec3d pos) {
		Vec3d vec3d = pos.subtract(this.getPos());
		double d = vec3d.horizontalLength();
		if (d > 12.0) {
			this.targetPos = this.getPos().add(vec3d.x / d * 12.0, 8.0, vec3d.z / d * 12.0);
		} else {
			this.targetPos = pos;
		}

		this.lifespan = 0;
	}
}
