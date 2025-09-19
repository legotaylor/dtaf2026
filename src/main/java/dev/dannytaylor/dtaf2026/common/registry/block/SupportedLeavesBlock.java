/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.dannytaylor.dtaf2026.common.registry.TagRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.ParticleUtil;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

public class SupportedLeavesBlock extends SupportedBlock implements Waterloggable {
	public static final MapCodec<SupportedLeavesBlock> codec = RecordCodecBuilder.mapCodec((instance) -> instance.group(Codecs.rangedInclusiveFloat(0.0F, 1.0F).fieldOf("leaf_particle_chance").forGetter((SupportedLeavesBlock) -> SupportedLeavesBlock.leafParticleChance), ParticleTypes.TYPE_CODEC.fieldOf("leaf_particle").forGetter((SupportedLeavesBlock) -> SupportedLeavesBlock.leafParticleEffect), createSettingsCodec()).apply(instance, SupportedLeavesBlock::new));
	protected final float leafParticleChance;
	protected final ParticleEffect leafParticleEffect;
	public static final BooleanProperty waterlogged = Properties.WATERLOGGED;

	public MapCodec<? extends SupportedLeavesBlock> getCodec() {
		return codec;
	}

	public SupportedLeavesBlock(float leafParticleChance, ParticleEffect leafParticleEffect, AbstractBlock.Settings settings) {
		super(settings);
		this.leafParticleChance = leafParticleChance;
		this.leafParticleEffect = leafParticleEffect;
	}

	protected VoxelShape getSidesShape(BlockState state, BlockView world, BlockPos pos) {
		return VoxelShapes.empty();
	}

	protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
		BlockState blockState = super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
		if (blockState.get(waterlogged)) tickView.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		return blockState;
	}

	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(waterlogged);
	}

	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return super.getPlacementState(ctx).with(waterlogged, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER);
	}

	protected FluidState getFluidState(BlockState state) {
		return state.get(waterlogged) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		super.randomDisplayTick(state, world, pos, random);
		BlockPos blockPos = pos.down();
		BlockState blockState = world.getBlockState(blockPos);
		spawnWaterParticle(world, pos, random, blockState, blockPos);
		this.spawnLeafParticle(world, pos, random, blockState, blockPos);
	}

	private static void spawnWaterParticle(World world, BlockPos pos, Random random, BlockState state, BlockPos posBelow) {
		if (world.hasRain(pos.up())) {
			if (random.nextInt(15) == 1) {
				if (!state.isOpaque() || !state.isSideSolidFullSquare(world, posBelow, Direction.UP)) {
					ParticleUtil.spawnParticle(world, pos, random, ParticleTypes.DRIPPING_WATER);
				}
			}
		}
	}

	private void spawnLeafParticle(World world, BlockPos pos, Random random, BlockState state, BlockPos posBelow) {
		if (!(random.nextFloat() >= this.leafParticleChance)) {
			if (!isFaceFullSquare(state.getCollisionShape(world, posBelow), Direction.UP)) {
				this.spawnLeafParticle(world, pos, random);
			}
		}
	}

	public void spawnLeafParticle(World world, BlockPos pos, Random random) {
		ParticleUtil.spawnParticle(world, pos, random, this.leafParticleEffect);
	}

	public IsOfBlock isOf() {
		return (state -> state.isIn(TagRegistry.Block.supportsSupportedLeaves));
	}
}
