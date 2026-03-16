/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.entity.fleecifer;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FleeciferEntity extends HostileEntity {
	private static final TrackedData<Boolean> rainbow;

	static {
		rainbow = DataTracker.registerData(FleeciferEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	}

	public FleeciferEntity(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
	}

	public static DefaultAttributeContainer.Builder createFleeciferAttributes() {
		return HostileEntity.createHostileAttributes().add(EntityAttributes.FOLLOW_RANGE, 35.0F).add(EntityAttributes.MOVEMENT_SPEED, 0.23F).add(EntityAttributes.ATTACK_DAMAGE, 3.0F).add(EntityAttributes.ARMOR, 2.0F).add(EntityAttributes.SPAWN_REINFORCEMENTS);
	}

	public void initGoals() {
		this.goalSelector.add(0, new SwimGoal(this));
		this.goalSelector.add(1, new FleeciferAttackGoal(this, 1.0F, false));
		this.goalSelector.add(2, new WanderAroundFarGoal(this, 1.0F));
		this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.add(3, new LookAroundGoal(this));
		this.targetSelector.add(1, new RevengeGoal(this).setGroupRevenge(FleeciferEntity.class));
		this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
	}

	public void initDataTracker(DataTracker.Builder builder) {
		super.initDataTracker(builder);
		builder.add(rainbow, false);
	}

	@Override
	public boolean shouldDropLoot() {
		return !isRainbow();
	}

	public boolean isRainbow() {
		return this.getDataTracker().get(rainbow);
	}

	public void setPreventLoot(boolean value) {
		this.getDataTracker().set(rainbow, value);
	}

	public void writeCustomData(WriteView view) {
		super.writeCustomData(view);
		view.putBoolean("Rainbow", this.isRainbow());
	}

	public void readCustomData(ReadView view) {
		super.readCustomData(view);
		this.setPreventLoot(view.getBoolean("Rainbow", false));
	}

	public SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_SHEEP_AMBIENT;
	}

	public SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.ENTITY_SHEEP_HURT;
	}

	public SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_SHEEP_DEATH;
	}

	public void playStepSound(BlockPos pos, BlockState state) {
		this.playSound(SoundEvents.ENTITY_SHEEP_STEP, 0.15F, 1.0F);
	}

	public static class FleeciferAttackGoal extends MeleeAttackGoal {
		private final FleeciferEntity fleecifer;
		private int ticks;

		public FleeciferAttackGoal(FleeciferEntity fleecifer, double speed, boolean pauseWhenMobIdle) {
			super(fleecifer, speed, pauseWhenMobIdle);
			this.fleecifer = fleecifer;
		}

		public void start() {
			super.start();
			this.ticks = 0;
		}

		public void stop() {
			super.stop();
			this.fleecifer.setAttacking(false);
		}

		public void tick() {
			super.tick();
			this.ticks++;
			this.fleecifer.setAttacking(this.ticks >= 5 && this.getCooldown() < this.getMaxCooldown() / 2);
		}
	}
}
