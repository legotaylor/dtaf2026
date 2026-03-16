/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.entity.boar;

import dev.dannytaylor.dtaf2026.common.data.Data;
import dev.dannytaylor.dtaf2026.common.registry.DataComponentRegistry;
import dev.dannytaylor.dtaf2026.common.registry.EntityRegistry;
import dev.dannytaylor.dtaf2026.common.registry.TagRegistry;
import dev.dannytaylor.dtaf2026.common.registry.TrackedDataRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.component.ComponentType;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.conversion.EntityConversionContext;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.spawn.SpawnContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BoarEntity extends AnimalEntity implements ItemSteerable {
	private static final TrackedData<Integer> boostTime;
	private static final TrackedData<Identifier> variant;

	static {
		boostTime = DataTracker.registerData(BoarEntity.class, TrackedDataHandlerRegistry.INTEGER);
		variant = DataTracker.registerData(BoarEntity.class, TrackedDataRegistry.variant);
	}

	private final SaddledComponent saddledComponent;

	public BoarEntity(EntityType<? extends BoarEntity> entityType, World world) {
		super(entityType, world);
		this.saddledComponent = new SaddledComponent(this.dataTracker, boostTime);
	}

	public static DefaultAttributeContainer.Builder createBoarAttributes() {
		return AnimalEntity.createAnimalAttributes().add(EntityAttributes.MAX_HEALTH, 15.0F).add(EntityAttributes.MOVEMENT_SPEED, 0.25F);
	}

	public void initGoals() {
		this.goalSelector.add(0, new SwimGoal(this));
		this.goalSelector.add(1, new EscapeDangerGoal(this, 1.3F));
		this.goalSelector.add(3, new AnimalMateGoal(this, 1.05F));
		this.goalSelector.add(4, new TemptGoal(this, 1.25, (stack) -> stack.isOf(Items.CARROT_ON_A_STICK), false));
		this.goalSelector.add(4, new TemptGoal(this, 1.25, (stack) -> stack.isIn(TagRegistry.Item.boarFood), false));
		this.goalSelector.add(5, new FollowParentGoal(this, 1.15));
		this.goalSelector.add(6, new WanderAroundFarGoal(this, 1.05F));
		this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 3.0F));
		this.goalSelector.add(8, new LookAroundGoal(this));
	}

	@Nullable
	public LivingEntity getControllingPassenger() {
		if (this.hasSaddleEquipped()) {
			Entity var2 = this.getFirstPassenger();
			if (var2 instanceof PlayerEntity playerEntity) {
				if (playerEntity.isHolding(Items.CARROT_ON_A_STICK)) {
					return playerEntity;
				}
			}
		}

		return super.getControllingPassenger();
	}

	public void onTrackedDataSet(TrackedData<?> data) {
		if (boostTime.equals(data) && this.getWorld().isClient) {
			this.saddledComponent.boost();
		}

		super.onTrackedDataSet(data);
	}

	public BoarEntity createChild(ServerWorld serverWorld, PassiveEntity passiveEntity) {
		BoarEntity boarEntity = EntityRegistry.boar.create(serverWorld, SpawnReason.BREEDING);
		if (boarEntity != null && passiveEntity instanceof BoarEntity boarEntity1)
			boarEntity.setVariant(this.random.nextBoolean() ? this.getVariant() : boarEntity1.getVariant());
		return boarEntity;
	}

	public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
		EntityData data = super.initialize(world, difficulty, spawnReason, entityData);
		BoarVariants.select(world.getRandom(), SpawnContext.of(world, this.getBlockPos())).ifPresent(this::setVariant);
		return data;
	}

	public void readCustomData(ReadView view) {
		super.readCustomData(view);
		setVariant(BoarVariants.variants.getOrDefaultId(Identifier.of(view.getString("Variant", Data.idOf("temperate").toString()))));
	}

	public void writeCustomData(WriteView view) {
		super.writeCustomData(view);
		view.putString("Variant", this.getVariant().toString());
	}

	public Identifier getVariant() {
		return this.dataTracker.get(variant);
	}

	public void setVariant(Identifier variant) {
		this.dataTracker.set(BoarEntity.variant, variant);
	}

	public BoarVariant getVariantData() {
		return BoarVariants.variants.getOrDefault(getVariant(), BoarVariants.variants.getDefault());
	}

	@Nullable
	public <T> T get(ComponentType<? extends T> type) {
		return type == DataComponentRegistry.variant ? castComponentValue(type, this.getVariant()) : super.get(type);
	}

	public <T> boolean setApplicableComponent(ComponentType<T> type, T value) {
		if (type == DataComponentRegistry.variant) {
			this.setVariant(castComponentValue(DataComponentRegistry.variant, value));
			return true;
		} else {
			return super.setApplicableComponent(type, value);
		}
	}

	public void initDataTracker(DataTracker.Builder builder) {
		super.initDataTracker(builder);
		builder.add(boostTime, 0);
		builder.add(variant, Data.idOf("temperate"));
	}

	public SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_PIG_AMBIENT;
	}

	public SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.ENTITY_PIG_HURT;
	}

	public SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_PIG_DEATH;
	}

	public void playStepSound(BlockPos pos, BlockState state) {
		this.playSound(SoundEvents.ENTITY_PIG_STEP, 0.15F, 1.0F);
	}

	public ActionResult interactMob(PlayerEntity player, Hand hand) {
		boolean bl = this.isBreedingItem(player.getStackInHand(hand));
		if (!bl && this.hasSaddleEquipped() && !this.hasPassengers() && !player.shouldCancelInteraction()) {
			if (!this.getWorld().isClient) {
				player.startRiding(this);
			}
			return ActionResult.SUCCESS;
		} else {
			ActionResult actionResult = super.interactMob(player, hand);
			if (!actionResult.isAccepted()) {
				ItemStack itemStack = player.getStackInHand(hand);
				return this.canEquip(itemStack, EquipmentSlot.SADDLE) ? itemStack.useOnEntity(player, this, hand) : ActionResult.PASS;
			} else {
				return actionResult;
			}
		}
	}

	public boolean canUseSlot(EquipmentSlot slot) {
		if (slot != EquipmentSlot.SADDLE) {
			return super.canUseSlot(slot);
		} else {
			return this.isAlive() && !this.isBaby();
		}
	}

	public boolean canDispenserEquipSlot(EquipmentSlot slot) {
		return slot == EquipmentSlot.SADDLE || super.canDispenserEquipSlot(slot);
	}

	public RegistryEntry<SoundEvent> getEquipSound(EquipmentSlot slot, ItemStack stack, EquippableComponent equippableComponent) {
		return slot == EquipmentSlot.SADDLE ? SoundEvents.ENTITY_PIG_SADDLE : super.getEquipSound(slot, stack, equippableComponent);
	}

	public Vec3d updatePassengerForDismount(LivingEntity passenger) {
		Direction direction = this.getMovementDirection();
		if (direction.getAxis() != Direction.Axis.Y) {
			int[][] is = Dismounting.getDismountOffsets(direction);
			BlockPos blockPos = this.getBlockPos();
			BlockPos.Mutable mutable = new BlockPos.Mutable();

			for (EntityPose entityPose : passenger.getPoses()) {
				Box box = passenger.getBoundingBox(entityPose);

				for (int[] js : is) {
					mutable.set(blockPos.getX() + js[0], blockPos.getY(), blockPos.getZ() + js[1]);
					double d = this.getWorld().getDismountHeight(mutable);
					if (Dismounting.canDismountInBlock(d)) {
						Vec3d vec3d = Vec3d.ofCenter(mutable, d);
						if (Dismounting.canPlaceEntityAt(this.getWorld(), passenger, box.offset(vec3d))) {
							passenger.setPose(entityPose);
							return vec3d;
						}
					}
				}
			}

		}
		return super.updatePassengerForDismount(passenger);
	}

	public void onStruckByLightning(ServerWorld world, LightningEntity lightning) {
		if (world.getDifficulty() != Difficulty.PEACEFUL) {
			ZombifiedPiglinEntity zombifiedPiglinEntity = this.convertTo(EntityType.ZOMBIFIED_PIGLIN, EntityConversionContext.create(this, false, true), (zombifiedPiglin) -> {
				if (this.getMainHandStack().isEmpty()) {
					zombifiedPiglin.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
				}
				zombifiedPiglin.setPersistent();
			});
			if (zombifiedPiglinEntity == null) {
				super.onStruckByLightning(world, lightning);
			}
		} else {
			super.onStruckByLightning(world, lightning);
		}
	}

	public void tickControlled(PlayerEntity controllingPlayer, Vec3d movementInput) {
		super.tickControlled(controllingPlayer, movementInput);
		this.setRotation(controllingPlayer.getYaw(), controllingPlayer.getPitch() * 0.5F);
		this.lastYaw = this.bodyYaw = this.headYaw = this.getYaw();
		this.saddledComponent.tickBoost();
	}

	public Vec3d getControlledMovementInput(PlayerEntity controllingPlayer, Vec3d movementInput) {
		return new Vec3d(0.0F, 0.0F, 1.0F);
	}

	public float getSaddledSpeed(PlayerEntity controllingPlayer) {
		return (float) (this.getAttributeValue(EntityAttributes.MOVEMENT_SPEED) * 0.225 * (double) this.saddledComponent.getMovementSpeedMultiplier());
	}

	public boolean consumeOnAStickItem() {
		return this.saddledComponent.boost(this.getRandom());
	}

	public boolean isBreedingItem(ItemStack stack) {
		return stack.isIn(TagRegistry.Item.boarFood);
	}

	public Vec3d getLeashOffset() {
		return new Vec3d(0.0F, 0.6F * this.getStandingEyeHeight(), this.getWidth() * 0.4F);
	}

	public void copyComponentsFrom(ComponentsAccess from) {
		this.copyComponentFrom(from, DataComponentRegistry.variant);
		super.copyComponentsFrom(from);
	}
}
