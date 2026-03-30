/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.*;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.StairShape;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.AxisRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.DirectionTransformation;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

import java.util.Map;

public class SupportedStairsBlock extends SupportedBlock implements Waterloggable {
	public static final EnumProperty<Direction> facing;
	public static final EnumProperty<BlockHalf> half;
	public static final EnumProperty<StairShape> shape;
	public static final BooleanProperty waterlogged;
	public static final MapCodec<SupportedStairsBlock> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
		BlockState.CODEC.fieldOf("base_state").forGetter((block) -> block.baseBlockState),
		TagKey.codec(RegistryKeys.BLOCK).fieldOf("isOf").forGetter(block -> block.isOf),
		createSettingsCodec()).apply(instance, SupportedStairsBlock::new)
	);
	private static final VoxelShape OUTER_SHAPE;
	private static final VoxelShape STRAIGHT_SHAPE;
	private static final VoxelShape INNER_SHAPE;
	private static final Map<Direction, VoxelShape> OUTER_BOTTOM_SHAPES;
	private static final Map<Direction, VoxelShape> STRAIGHT_BOTTOM_SHAPES;
	private static final Map<Direction, VoxelShape> INNER_BOTTOM_SHAPES;
	private static final Map<Direction, VoxelShape> OUTER_TOP_SHAPES;
	private static final Map<Direction, VoxelShape> STRAIGHT_TOP_SHAPES;
	private static final Map<Direction, VoxelShape> INNER_TOP_SHAPES;

	static {
		facing = HorizontalFacingBlock.FACING;
		half = Properties.BLOCK_HALF;
		shape = Properties.STAIR_SHAPE;
		waterlogged = Properties.WATERLOGGED;
		OUTER_SHAPE = VoxelShapes.union(Block.createColumnShape(16.0F, 0.0F, 8.0F), Block.createCuboidShape(0.0F, 8.0F, 0.0F, 8.0F, 16.0F, 8.0F));
		STRAIGHT_SHAPE = VoxelShapes.union(OUTER_SHAPE, VoxelShapes.transform(OUTER_SHAPE, DirectionTransformation.fromRotations(AxisRotation.R0, AxisRotation.R90)));
		INNER_SHAPE = VoxelShapes.union(STRAIGHT_SHAPE, VoxelShapes.transform(STRAIGHT_SHAPE, DirectionTransformation.fromRotations(AxisRotation.R0, AxisRotation.R90)));
		OUTER_BOTTOM_SHAPES = VoxelShapes.createHorizontalFacingShapeMap(OUTER_SHAPE);
		STRAIGHT_BOTTOM_SHAPES = VoxelShapes.createHorizontalFacingShapeMap(STRAIGHT_SHAPE);
		INNER_BOTTOM_SHAPES = VoxelShapes.createHorizontalFacingShapeMap(INNER_SHAPE);
		OUTER_TOP_SHAPES = VoxelShapes.createHorizontalFacingShapeMap(VoxelShapes.transform(OUTER_SHAPE, DirectionTransformation.INVERT_Y));
		STRAIGHT_TOP_SHAPES = VoxelShapes.createHorizontalFacingShapeMap(VoxelShapes.transform(STRAIGHT_SHAPE, DirectionTransformation.INVERT_Y));
		INNER_TOP_SHAPES = VoxelShapes.createHorizontalFacingShapeMap(VoxelShapes.transform(INNER_SHAPE, DirectionTransformation.INVERT_Y));
	}

	protected final BlockState baseBlockState;
	private final Block baseBlock;

	public SupportedStairsBlock(BlockState baseBlockState, TagKey<Block> isOf, AbstractBlock.Settings settings) {
		super(isOf, settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(facing, Direction.NORTH).with(half, BlockHalf.BOTTOM).with(shape, StairShape.STRAIGHT).with(waterlogged, false));
		this.baseBlock = baseBlockState.getBlock();
		this.baseBlockState = baseBlockState;
	}

	private static StairShape getStairShape(BlockState state, BlockView world, BlockPos pos) {
		Direction direction = state.get(facing);
		BlockState blockState = world.getBlockState(pos.offset(direction));
		if (isStairs(blockState) && state.get(half) == blockState.get(half)) {
			Direction direction2 = blockState.get(facing);
			if (direction2.getAxis() != state.get(facing).getAxis() && isDifferentOrientation(state, world, pos, direction2.getOpposite())) {
				if (direction2 == direction.rotateYCounterclockwise()) {
					return StairShape.OUTER_LEFT;
				}

				return StairShape.OUTER_RIGHT;
			}
		}

		BlockState blockState2 = world.getBlockState(pos.offset(direction.getOpposite()));
		if (isStairs(blockState2) && state.get(half) == blockState2.get(half)) {
			Direction direction3 = blockState2.get(facing);
			if (direction3.getAxis() != state.get(facing).getAxis() && isDifferentOrientation(state, world, pos, direction3)) {
				if (direction3 == direction.rotateYCounterclockwise()) {
					return StairShape.INNER_LEFT;
				}

				return StairShape.INNER_RIGHT;
			}
		}

		return StairShape.STRAIGHT;
	}

	private static boolean isDifferentOrientation(BlockState state, BlockView world, BlockPos pos, Direction dir) {
		BlockState blockState = world.getBlockState(pos.offset(dir));
		return !isStairs(blockState) || blockState.get(facing) != state.get(facing) || blockState.get(half) != state.get(half);
	}

	public static boolean isStairs(BlockState state) {
		return state.getBlock() instanceof SupportedStairsBlock || state.getBlock() instanceof StairsBlock;
	}

	public MapCodec<? extends SupportedStairsBlock> getCodec() {
		return CODEC;
	}

	protected boolean hasSidedTransparency(BlockState state) {
		return true;
	}

	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		boolean bl = state.get(half) == BlockHalf.BOTTOM;
		Direction direction = state.get(facing);
		Map<Direction, VoxelShape> directionVoxelShapeMap = switch (state.get(shape)) {
			case STRAIGHT -> bl ? STRAIGHT_BOTTOM_SHAPES : STRAIGHT_TOP_SHAPES;
			case OUTER_LEFT, OUTER_RIGHT -> bl ? OUTER_BOTTOM_SHAPES : OUTER_TOP_SHAPES;
			case INNER_RIGHT, INNER_LEFT -> bl ? INNER_BOTTOM_SHAPES : INNER_TOP_SHAPES;
		};

		Direction directionFromShape = switch (state.get(shape)) {
			case STRAIGHT, OUTER_LEFT, INNER_RIGHT -> direction;
			case INNER_LEFT -> direction.rotateYCounterclockwise();
			case OUTER_RIGHT -> direction.rotateYClockwise();
		};

		return directionVoxelShapeMap.get(directionFromShape);
	}

	public float getBlastResistance() {
		return this.baseBlock.getBlastResistance();
	}

	public BlockState getPlacementState(ItemPlacementContext ctx) {
		Direction direction = ctx.getSide();
		BlockPos blockPos = ctx.getBlockPos();
		FluidState fluidState = ctx.getWorld().getFluidState(blockPos);
		BlockState blockState = this.getDefaultState().with(facing, ctx.getHorizontalPlayerFacing()).with(half, direction != Direction.DOWN && (direction == Direction.UP || !(ctx.getHitPos().y - (double) blockPos.getY() > (double) 0.5F)) ? BlockHalf.BOTTOM : BlockHalf.TOP).with(waterlogged, fluidState.getFluid() == Fluids.WATER);
		return blockState.with(shape, getStairShape(blockState, ctx.getWorld(), blockPos));
	}

	protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
		if (state.get(waterlogged)) {
			tickView.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}

		return super.getStateForNeighborUpdate(direction.getAxis().isHorizontal() ? state.with(shape, getStairShape(state, world, pos)) : state, world, tickView, pos, direction, neighborPos, neighborState, random);
	}

	protected BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(facing, rotation.rotate(state.get(facing)));
	}

	protected BlockState mirror(BlockState state, BlockMirror mirror) {
		Direction direction = state.get(facing);
		StairShape stairShape = state.get(shape);
		switch (mirror) {
			case LEFT_RIGHT:
				if (direction.getAxis() == Direction.Axis.Z) {
					switch (stairShape) {
						case OUTER_LEFT -> {
							return state.rotate(BlockRotation.CLOCKWISE_180).with(shape, StairShape.OUTER_RIGHT);
						}
						case INNER_RIGHT -> {
							return state.rotate(BlockRotation.CLOCKWISE_180).with(shape, StairShape.INNER_LEFT);
						}
						case INNER_LEFT -> {
							return state.rotate(BlockRotation.CLOCKWISE_180).with(shape, StairShape.INNER_RIGHT);
						}
						case OUTER_RIGHT -> {
							return state.rotate(BlockRotation.CLOCKWISE_180).with(shape, StairShape.OUTER_LEFT);
						}
						default -> {
							return state.rotate(BlockRotation.CLOCKWISE_180);
						}
					}
				}
				break;
			case FRONT_BACK:
				if (direction.getAxis() == Direction.Axis.X) {
					switch (stairShape) {
						case STRAIGHT -> {
							return state.rotate(BlockRotation.CLOCKWISE_180);
						}
						case OUTER_LEFT -> {
							return state.rotate(BlockRotation.CLOCKWISE_180).with(shape, StairShape.OUTER_RIGHT);
						}
						case INNER_RIGHT -> {
							return state.rotate(BlockRotation.CLOCKWISE_180).with(shape, StairShape.INNER_RIGHT);
						}
						case INNER_LEFT -> {
							return state.rotate(BlockRotation.CLOCKWISE_180).with(shape, StairShape.INNER_LEFT);
						}
						case OUTER_RIGHT -> {
							return state.rotate(BlockRotation.CLOCKWISE_180).with(shape, StairShape.OUTER_LEFT);
						}
					}
				}
		}

		return super.mirror(state, mirror);
	}

	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(facing, half, shape, waterlogged);
	}

	protected FluidState getFluidState(BlockState state) {
		return state.get(waterlogged) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	protected boolean canPathfindThrough(BlockState state, NavigationType type) {
		return false;
	}
}
