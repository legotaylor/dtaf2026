/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.dannytaylor.dtaf2026.common.registry.EntityRegistry;
import dev.dannytaylor.dtaf2026.common.registry.TagRegistry;
import dev.dannytaylor.dtaf2026.common.registry.entity.junglefowl.JunglefowlEntity;
import dev.dannytaylor.dtaf2026.common.registry.entity.junglefowl.JunglefowlVariant;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;

public class JunglefowlEggBlock extends Block {
	public static final IntProperty hatch = Properties.HATCH;
	public static final MapCodec<JunglefowlEggBlock> codec = RecordCodecBuilder.mapCodec(
		instance -> instance.group(
				Codec.DOUBLE.fieldOf("sizeX").forGetter(block -> block.sizeX),
				Codec.DOUBLE.fieldOf("sizeZ").forGetter(block -> block.sizeZ),
				Codec.DOUBLE.fieldOf("minY").forGetter(block -> block.minY),
				Codec.DOUBLE.fieldOf("maxY").forGetter(block -> block.maxY),
				Identifier.CODEC.fieldOf("variantId").forGetter(block -> block.variantId),
				createSettingsCodec()
			)
			.apply(instance, JunglefowlEggBlock::new)
	);
	private final VoxelShape shape;
	private final Identifier variantId;

	private final double sizeX;
	private final double sizeZ;
	private final double minY;
	private final double maxY;

	private JunglefowlEggBlock(double sizeX, double sizeZ, double minY, double maxY, Identifier variantId, AbstractBlock.Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(hatch, 0));
		this.shape = Block.createColumnShape(sizeX, sizeZ, minY, maxY);
		this.variantId = variantId;

		this.sizeX = sizeX;
		this.sizeZ = sizeZ;
		this.minY = minY;
		this.maxY = maxY;
	}

	public static JunglefowlEggBlock of(AbstractBlock.Settings settings, JunglefowlVariant.Model model, Identifier variantId) {
		return switch (model) {
			case big -> new JunglefowlEggBlock(8.0, 8.0, 0.0, 12.0, variantId, settings);
			case small -> new JunglefowlEggBlock(6.0, 6.0, 0.0, 8.0, variantId, settings);
		};
	}

	public static boolean isAboveHatchBooster(BlockView world, BlockPos pos) {
		return world.getBlockState(pos.down()).isIn(TagRegistry.Block.junglefowlEggHatchBooster);
	}

	public MapCodec<JunglefowlEggBlock> getCodec() {
		return codec;
	}

	public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
		if (!entity.bypassesSteppingEffects()) this.tryBreakEgg(world, state, pos, entity, 100);
		super.onSteppedOn(world, pos, state, entity);
	}

	public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, double fallDistance) {
		if (!(entity instanceof ZombieEntity)) this.tryBreakEgg(world, state, pos, entity, 3);
		super.onLandedUpon(world, state, pos, entity, fallDistance);
	}

	public void tryBreakEgg(World world, BlockState state, BlockPos pos, Entity entity, int inverseChance) {
		if (world instanceof ServerWorld serverWorld && this.breaksEgg(serverWorld, entity) && world.random.nextInt(inverseChance) == 0)
			this.breakEgg(serverWorld, pos, state);
	}

	public void breakEgg(World world, BlockPos pos, BlockState state) {
		world.playSound(null, pos, SoundEvents.ENTITY_TURTLE_EGG_BREAK, SoundCategory.BLOCKS, 0.7F, 0.9F + world.random.nextFloat() * 0.2F);
		world.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_LISTENERS);
		world.emitGameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Emitter.of(state));
		world.syncWorldEvent(WorldEvents.BLOCK_BROKEN, pos, Block.getRawIdFromState(state));
	}

	public boolean breaksEgg(ServerWorld world, Entity entity) {
		if (entity instanceof JunglefowlEntity || entity instanceof BatEntity) return false;
		if (entity instanceof LivingEntity)
			return entity instanceof PlayerEntity || world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING);
		return false;
	}

	public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(hatch);
	}

	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return shape;
	}

	public int getHatchStage(BlockState state) {
		return state.get(hatch);
	}

	private boolean isReadyToHatch(BlockState state) {
		return this.getHatchStage(state) == 2;
	}

	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (!this.isReadyToHatch(state)) {
			world.playSound(null, pos, SoundEvents.BLOCK_SNIFFER_EGG_CRACK, SoundCategory.BLOCKS, 0.7F, 0.9F + random.nextFloat() * 0.2F);
			world.setBlockState(pos, state.with(hatch, this.getHatchStage(state) + 1), Block.NOTIFY_LISTENERS);
			return;
		}
		world.playSound(null, pos, SoundEvents.BLOCK_SNIFFER_EGG_HATCH, SoundCategory.BLOCKS, 0.7F, 0.9F + random.nextFloat() * 0.2F);
		world.breakBlock(pos, false);
		JunglefowlEntity junglefowlEntity = EntityRegistry.junglefowl.create(world, SpawnReason.BREEDING);
		if (junglefowlEntity != null) {
			Vec3d centerPos = pos.toCenterPos();
			junglefowlEntity.setBaby(true);
			junglefowlEntity.refreshPositionAndAngles(centerPos.getX(), centerPos.getY(), centerPos.getZ(), MathHelper.wrapDegrees(world.random.nextFloat() * 360.0F), 0.0F);
			junglefowlEntity.setVariant(this.variantId);
			world.spawnEntity(junglefowlEntity);
		}
	}

	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		boolean isAboveHatchBooster = isAboveHatchBooster(world, pos);
		if (!world.isClient() && isAboveHatchBooster) world.syncWorldEvent(WorldEvents.SNIFFER_EGG_CRACKS, pos, 0);
		world.emitGameEvent(GameEvent.BLOCK_PLACE, pos, GameEvent.Emitter.of(state));
		world.scheduleBlockTick(pos, this, (isAboveHatchBooster ? 6000 : 12000) / 3 + world.random.nextInt(300));
	}

	public boolean canPathfindThrough(BlockState state, NavigationType type) {
		return false;
	}
}
