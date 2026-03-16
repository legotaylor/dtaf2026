/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.entity.fleecifer;

import dev.dannytaylor.dtaf2026.common.registry.EntityRegistry;
import dev.dannytaylor.dtaf2026.common.registry.WaypointStyleRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.FuzzyTargeting;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class FleeciferBossEntity extends HostileEntity {
	private static final TrackedData<Boolean> isBeaming;
	private static final TrackedData<Integer> targetId;
	private static final TrackedData<Integer> phase;

	static {
		isBeaming = DataTracker.registerData(FleeciferBossEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
		targetId = DataTracker.registerData(FleeciferBossEntity.class, TrackedDataHandlerRegistry.INTEGER);
		phase = DataTracker.registerData(FleeciferBossEntity.class, TrackedDataHandlerRegistry.INTEGER);
	}

	public int ticksSinceDeath = 0;
	@Nullable
	private FleeciferFight fight;
	private int beamTicks;
	private int beamCooldown = 150;
	@Nullable
	private LivingEntity cachedBeamTarget;

	public FleeciferBossEntity(EntityType<? extends FleeciferBossEntity> entityType, World world) {
		super(entityType, world);
		this.waypointConfig.style = WaypointStyleRegistry.fleeciferBoss;
	}

	public static DefaultAttributeContainer.Builder createFleeciferAttributes() {
		return HostileEntity.createHostileAttributes().add(EntityAttributes.MAX_HEALTH, 1024.0F).add(EntityAttributes.MOVEMENT_SPEED, 0.46F).add(EntityAttributes.SCALE, 4.5F).add(EntityAttributes.ATTACK_SPEED, 16.0F).add(EntityAttributes.ATTACK_KNOCKBACK, 1.0F).add(EntityAttributes.FOLLOW_RANGE, 92.0F).add(EntityAttributes.WAYPOINT_TRANSMIT_RANGE, 192);
	}

	public static boolean isBeaming(FleeciferBossEntity fleecifer) {
		return fleecifer.dataTracker.get(isBeaming);
	}

	public static void setBeaming(FleeciferBossEntity fleecifer, boolean value) {
		fleecifer.dataTracker.set(isBeaming, value);
	}

	public static void setPhase(FleeciferBossEntity fleecifer, int value) {
		fleecifer.dataTracker.set(phase, value);
	}

	public static int getPhase(FleeciferBossEntity fleecifer) {
		return fleecifer.dataTracker.get(phase);
	}

	public boolean hasBeamTarget() {
		return this.dataTracker.get(targetId) != 0;
	}

	@Nullable
	public LivingEntity getBeamTarget() {
		if (!this.hasBeamTarget()) {
			return null;
		} else if (this.getWorld().isClient) {
			if (this.cachedBeamTarget != null) {
				return this.cachedBeamTarget;
			} else {
				Entity entity = this.getWorld().getEntityById(this.dataTracker.get(targetId));
				if (entity instanceof LivingEntity) {
					this.cachedBeamTarget = (LivingEntity) entity;
					return this.cachedBeamTarget;
				} else {
					return null;
				}
			}
		} else {
			return this.getTarget();
		}
	}

	public void setBeamTarget(int value) {
		this.dataTracker.set(targetId, value);
	}

	@Override
	protected void applyDamage(ServerWorld world, DamageSource source, float amount) {
		if (!isBeaming(this)) super.applyDamage(world, source, amount);
	}

	public void setFight(@Nullable FleeciferFight fight) {
		this.fight = fight;
		this.setPersistent();
	}

	public int getBeamTicks() {
		return this.beamTicks;
	}

	public float getBeamProgress(float tickProgress) {
		return ((float) this.beamTicks + tickProgress) / (float) this.getWarmupTime();
	}

	@Override
	protected void initGoals() {
		this.goalSelector.add(0, new SwimGoal(this));
		this.goalSelector.add(1, new BeamGoal(this));
		this.goalSelector.add(2, new FleeciferAttackGoal(this, 1.0F, false));
		this.goalSelector.add(3, new FleeciferBossWanderAroundGoal(this, 0.75F));
		this.goalSelector.add(4, new LookAtEntityGoal(this, PlayerEntity.class, 92.0F));
		this.goalSelector.add(5, new LookAroundGoal(this));
		this.targetSelector.add(0, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
	}

	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_SHEEP_AMBIENT;
	}

	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.ENTITY_SHEEP_HURT;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_SHEEP_DEATH;
	}

	protected void playStepSound(BlockPos pos, BlockState state) {
		this.playSound(SoundEvents.ENTITY_SHEEP_STEP, 0.15F, 1.0F);
	}

	public int getWarmupTime() {
		return 40;
	}

	public int getMaxTime() {
		return 160;
	}

	@Override
	protected void mobTick(ServerWorld world) {
		super.mobTick(world);
		if (this.age % 100 == 0) this.heal(this.random.nextBetween(5, 20) * (isBeaming(this) ? 0.5F : 1.0F));
		setBeamTarget(this.getTarget() != null ? this.getTarget().getId() : 0);
		if (this.beamCooldown > 0) this.beamCooldown--;
		if (this.fight == null) {
			if (EntityRegistry.getFleeciferFights().containsKey(this.getUuid()))
				setFight(EntityRegistry.getFleeciferFights().get(this.getUuid()));
			else
				setFight(EntityRegistry.addFleeciferFight(new FleeciferFight(world, this.getBlockPos(), this.getUuid())));
		} else this.fight.updateFight(this);
	}

	@Override
	public void kill(ServerWorld world) {
		this.remove(RemovalReason.KILLED);
		this.emitGameEvent(GameEvent.ENTITY_DIE);

		if (this.fight != null) {
			this.fight.updateFight(this);
			this.fight.fleeciferKilled(this);
		}
	}

	@Override
	protected void updatePostDeath() {
		if (this.fight != null) this.fight.updateFight(this);
		this.ticksSinceDeath++;
		float f = (this.random.nextFloat() - 0.5F) * 8.0F;
		float g = (this.random.nextFloat() - 0.5F) * 4.0F;
		float h = (this.random.nextFloat() - 0.5F) * 8.0F;
		this.getWorld().addParticleClient(ParticleTypes.EXPLOSION_EMITTER, this.getX() + f, this.getY() + 2.0F + g, this.getZ() + h, 0, 0, 0);
		this.getWorld().playSoundClient(SoundEvents.ENTITY_SHEEP_DEATH, SoundCategory.HOSTILE, 1.0F, 1.0F);
		if (this.ticksSinceDeath == 20) {
			if (this.getWorld() instanceof ServerWorld serverWorld) {
				if (serverWorld.getGameRules().getBoolean(GameRules.DO_MOB_LOOT))
					ExperienceOrbEntity.spawn(serverWorld, this.getPos(), 12000);
				if (this.fight != null) this.fight.fleeciferKilled(this);
				this.remove(RemovalReason.KILLED);
				this.emitGameEvent(GameEvent.ENTITY_DIE);
			}
		}
	}

	protected void writeCustomData(WriteView view) {
		super.writeCustomData(view);
		view.putInt("DeathTime", this.ticksSinceDeath);
	}

	protected void readCustomData(ReadView view) {
		super.readCustomData(view);
		this.ticksSinceDeath = view.getInt("DeathTime", 0);
	}

	@Override
	protected void initDataTracker(DataTracker.Builder builder) {
		super.initDataTracker(builder);
		builder.add(isBeaming, false);
		builder.add(targetId, 0);
		builder.add(phase, 0);
	}

	public static class FleeciferAttackGoal extends MeleeAttackGoal {
		private final FleeciferBossEntity fleecifer;
		private int ticks;

		public FleeciferAttackGoal(FleeciferBossEntity fleecifer, double speed, boolean pauseWhenIdle) {
			super(fleecifer, speed, pauseWhenIdle);
			this.fleecifer = fleecifer;
		}

		public boolean canStart() {
			return !isBeaming(this.fleecifer) && super.canStart() && this.fleecifer.beamCooldown >= 80;
		}

		public boolean shouldContinue() {
			return !isBeaming(this.fleecifer) && super.shouldContinue() && this.fleecifer.beamCooldown <= 60;
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

	public static class BeamGoal extends Goal {
		private final FleeciferBossEntity fleecifer;

		public BeamGoal(FleeciferBossEntity fleecifer) {
			this.fleecifer = fleecifer;
			this.setControls(EnumSet.of(Control.MOVE, Control.LOOK, Control.JUMP, Control.TARGET));
		}

		public boolean canStart() {
			if (this.fleecifer.beamCooldown > 0) return false;
			LivingEntity target = this.fleecifer.getTarget();
			return target != null && target.isAlive();
		}

		public void start() {
			this.fleecifer.beamTicks = 0;
			this.fleecifer.getNavigation().stop();
			LivingEntity target = this.fleecifer.getTarget();
			if (target != null) this.fleecifer.getLookControl().lookAt(target, 90.0F, 90.0F);
			this.fleecifer.velocityDirty = true;
		}

		public void stop() {
			this.fleecifer.beamTicks = 0;
			this.fleecifer.beamCooldown = 300;
			setBeaming(this.fleecifer, false);
		}

		public boolean shouldRunEveryTick() {
			return true;
		}

		public void tick() {
			LivingEntity target = this.fleecifer.getTarget();
			if (target == null || !target.isAlive()) return;
			this.fleecifer.getLookControl().lookAt(target, 20.0F, 20.0F);
			this.fleecifer.beamTicks++;
			if (this.fleecifer.beamTicks >= this.fleecifer.getWarmupTime() && this.fleecifer.beamTicks < this.fleecifer.getMaxTime()) {
				setBeaming(this.fleecifer, true);
				float damage = (float) this.fleecifer.getAttributeValue(EntityAttributes.ATTACK_DAMAGE);
				if (this.fleecifer.getWorld().getDifficulty() == Difficulty.HARD) damage *= 2.0F;
				ServerWorld world = getServerWorld(this.fleecifer);
				target.damage(world, this.fleecifer.getDamageSources().mobAttack(this.fleecifer), damage);
				this.fleecifer.tryAttack(world, target);
			} else if (this.fleecifer.beamTicks >= this.fleecifer.getMaxTime()) stop();
		}
	}

	public static class FleeciferBossWanderAroundGoal extends WanderAroundGoal {
		public FleeciferBossWanderAroundGoal(FleeciferBossEntity mob, double speed) {
			super(mob, speed, 120, false);
		}

		@Nullable
		protected Vec3d getWanderTarget() {
			return FuzzyTargeting.find(this.mob, 20, 7);
		}
	}
}
