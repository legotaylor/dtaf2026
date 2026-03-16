/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.entity.junglefowl;

import dev.dannytaylor.dtaf2026.common.data.Data;
import dev.dannytaylor.dtaf2026.common.registry.*;
import net.minecraft.block.BlockState;
import net.minecraft.component.ComponentType;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.spawn.SpawnContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class JunglefowlEntity extends AnimalEntity {
	private static final EntityDimensions babyBaseDimensions;
	private static final TrackedData<Identifier> variant;

	static {
		babyBaseDimensions = EntityRegistry.junglefowl.getDimensions().scaled(0.5F).withEyeHeight(0.2975F);
		variant = DataTracker.registerData(JunglefowlEntity.class, TrackedDataRegistry.variant);
	}

	public float flapProgress;
	public float maxWingDeviation;
	public float lastMaxWingDeviation;
	public float lastFlapProgress;
	public float flapSpeed = 1.0F;
	private float maxSpeed = 1.0F;

	public JunglefowlEntity(EntityType<? extends JunglefowlEntity> entityType, World world) {
		super(entityType, world);
		this.setPathfindingPenalty(PathNodeType.WATER, 0.0F);
	}

	public static DefaultAttributeContainer.Builder createJunglefowlAttributes() {
		return AnimalEntity.createAnimalAttributes().add(EntityAttributes.MAX_HEALTH, 6.0F).add(EntityAttributes.MOVEMENT_SPEED, 0.25F).add(EntityAttributes.ARMOR, 1.0).add(EntityAttributes.SCALE, 1.35);
	}

	public void initGoals() {
		this.goalSelector.add(0, new SwimGoal(this));
		this.goalSelector.add(1, new EscapeDangerGoal(this, 1.4, DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES));
		this.goalSelector.add(1, new MeleeAttackGoal(this, 1.4, false));
		this.goalSelector.add(2, new AnimalMateGoal(this, 1.0F));
		this.goalSelector.add(3, new TemptGoal(this, 1.0F, this::isBreedingItem, false));
		this.goalSelector.add(4, new FollowParentGoal(this, 1.1));
		this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0F));
		this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
		this.goalSelector.add(7, new LookAroundGoal(this));
	}

	public EntityDimensions getBaseDimensions(EntityPose pose) {
		return this.isBaby() ? babyBaseDimensions : super.getBaseDimensions(pose);
	}

	public void tickMovement() {
		super.tickMovement();
		this.lastFlapProgress = this.flapProgress;
		this.lastMaxWingDeviation = this.maxWingDeviation;
		this.maxWingDeviation += (this.isOnGround() ? -1.0F : 4.0F) * 0.3F;
		this.maxWingDeviation = MathHelper.clamp(this.maxWingDeviation, 0.0F, 1.0F);
		if (!this.isOnGround() && this.flapSpeed < 1.0F) this.flapSpeed = 1.0F;
		this.flapSpeed *= 0.9F;
		Vec3d velocity = this.getVelocity();
		if (!this.isOnGround() && velocity.y < (double) 0.0F) this.setVelocity(velocity.multiply(1.0F, 0.6, 1.0F));
		this.flapProgress += this.flapSpeed * 2.0F;
	}

	public boolean isFlappingWings() {
		return this.speed > this.maxSpeed;
	}

	public void addFlapEffects() {
		this.maxSpeed = this.speed + this.maxWingDeviation / 2.0F;
	}

	public SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_CHICKEN_AMBIENT;
	}

	public SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.ENTITY_CHICKEN_HURT;
	}

	public SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_CHICKEN_DEATH;
	}

	public void playStepSound(BlockPos pos, BlockState state) {
		this.playSound(SoundEvents.ENTITY_CHICKEN_STEP, 0.15F, 1.0F);
	}

	public void breed(ServerWorld world, AnimalEntity other) {
		if (this.isAlive() && !this.isBaby()) {
			if (this.getWorld() instanceof ServerWorld serverWorld) {
				if (this.forEachGiftedItem(serverWorld, LootTableRegistry.junglefowlLayGameplay, this::dropStack)) {
					this.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
				}
			}
			this.breed(world, other, null);
		}
	}

	// This isn't used, we instead use breed(); to drop the egg.
	public JunglefowlEntity createChild(ServerWorld serverWorld, PassiveEntity passiveEntity) {
		JunglefowlEntity junglefowlEntity = EntityRegistry.junglefowl.create(serverWorld, SpawnReason.BREEDING);
		if (junglefowlEntity != null && passiveEntity instanceof JunglefowlEntity junglefowlEntity1)
			junglefowlEntity.setVariant(this.random.nextBoolean() ? this.getVariant() : junglefowlEntity1.getVariant());
		return junglefowlEntity;
	}

	public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
		EntityData data = super.initialize(world, difficulty, spawnReason, entityData);
		JunglefowlVariants.select(world.getRandom(), SpawnContext.of(world, this.getBlockPos())).ifPresent(this::setVariant);
		return data;
	}

	public boolean isBreedingItem(ItemStack stack) {
		return stack.isIn(TagRegistry.Item.junglefowlFood);
	}

	public void initDataTracker(DataTracker.Builder builder) {
		super.initDataTracker(builder);
		builder.add(variant, Data.idOf("red"));
	}

	public void readCustomData(ReadView view) {
		super.readCustomData(view);
		setVariant(JunglefowlVariants.variants.getOrDefaultId(Identifier.of(view.getString("Variant", Data.idOf("red").toString()))));
	}

	public void writeCustomData(WriteView view) {
		super.writeCustomData(view);
		view.putString("Variant", this.getVariant().toString());
	}

	public Identifier getVariant() {
		return this.dataTracker.get(variant);
	}

	public void setVariant(Identifier variant) {
		this.dataTracker.set(JunglefowlEntity.variant, variant);
	}

	public JunglefowlVariant getVariantData() {
		return JunglefowlVariants.variants.getOrDefault(getVariant(), JunglefowlVariants.variants.getDefault());
	}

	@Nullable
	public <T> T get(ComponentType<? extends T> type) {
		return type == DataComponentRegistry.variant ? castComponentValue(type, this.getVariant()) : super.get(type);
	}

	public void copyComponentsFrom(ComponentsAccess from) {
		this.copyComponentFrom(from, DataComponentRegistry.variant);
		super.copyComponentsFrom(from);
	}

	public <T> boolean setApplicableComponent(ComponentType<T> type, T value) {
		if (type == DataComponentRegistry.variant) {
			this.setVariant(castComponentValue(DataComponentRegistry.variant, value));
			return true;
		} else {
			return super.setApplicableComponent(type, value);
		}
	}

	public void updatePassengerPosition(Entity passenger, Entity.PositionUpdater positionUpdater) {
		super.updatePassengerPosition(passenger, positionUpdater);
		if (passenger instanceof LivingEntity) ((LivingEntity) passenger).bodyYaw = this.bodyYaw;
	}
}
