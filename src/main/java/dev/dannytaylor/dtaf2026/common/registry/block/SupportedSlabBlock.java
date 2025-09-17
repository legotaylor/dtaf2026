/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.enums.SlabType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.Nullable;

public class SupportedSlabBlock extends SupportedBlock implements Waterloggable {
	public static final MapCodec<? extends SupportedSlabBlock> codec = createCodec(SupportedSlabBlock::new);
	public static final EnumProperty<SlabType> type;
	public static final BooleanProperty waterlogged;
	private static final VoxelShape bottomShape;
	private static final VoxelShape topShape;

	public MapCodec<? extends SupportedBlock> getCodec() {
		return codec;
	}

	public SupportedSlabBlock(Settings settings) {
		super(settings);
	}

	protected boolean hasSidedTransparency(BlockState state) {
		return state.get(type) != SlabType.DOUBLE;
	}

	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(type, waterlogged);
	}

	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		VoxelShape var10000;
		switch (state.get(type)) {
			case TOP -> var10000 = topShape;
			case BOTTOM -> var10000 = bottomShape;
			case DOUBLE -> var10000 = VoxelShapes.fullCube();
			default -> throw new MatchException(null, null);
		}

		return var10000;
	}

	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockState state = super.getPlacementState(ctx);
		if (state != null) {
			if (state.isOf(this)) {
				state = state.with(type, SlabType.DOUBLE).with(waterlogged, false);
			} else {
				FluidState fluidState = super.getFluidState(state);
				BlockState blockState2 = this.getDefaultState().with(type, SlabType.BOTTOM).with(waterlogged, fluidState.getFluid() == Fluids.WATER);
				Direction direction = ctx.getSide();
				state = direction != Direction.DOWN && (direction == Direction.UP || !(ctx.getHitPos().y - (double)ctx.getBlockPos().getY() > (double)0.5F)) ? blockState2 : blockState2.with(type, SlabType.TOP);
			}
		}
		return state;
	}

	protected boolean canReplace(BlockState state, ItemPlacementContext context) {
		ItemStack itemStack = context.getStack();
		SlabType slabType = state.get(type);
		if (slabType != SlabType.DOUBLE && itemStack.isOf(this.asItem())) {
			if (context.canReplaceExisting()) {
				boolean bl = context.getHitPos().y - (double)context.getBlockPos().getY() > (double)0.5F;
				Direction direction = context.getSide();
				if (slabType == SlabType.BOTTOM) {
					return direction == Direction.UP || bl && direction.getAxis().isHorizontal();
				} else {
					return direction == Direction.DOWN || !bl && direction.getAxis().isHorizontal();
				}
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	protected FluidState getFluidState(BlockState state) {
		return state.get(waterlogged) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	public boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState) {
		return state.get(type) != SlabType.DOUBLE && Waterloggable.super.tryFillWithFluid(world, pos, state, fluidState);
	}

	public boolean canFillWithFluid(@Nullable LivingEntity filler, BlockView world, BlockPos pos, BlockState state, Fluid fluid) {
		return state.get(type) != SlabType.DOUBLE && Waterloggable.super.canFillWithFluid(filler, world, pos, state, fluid);
	}

	protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
		if (state.get(waterlogged)) {
			tickView.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}

		return super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
	}

	protected boolean canPathfindThrough(BlockState state, NavigationType type) {
		return type.equals(NavigationType.WATER) && state.getFluidState().isIn(FluidTags.WATER);
	}

	public IsOfBlock isOf() {
		return (state -> state.isOf(this) && state.get(type).equals(SlabType.DOUBLE));
	}

	static {
		type = Properties.SLAB_TYPE;
		waterlogged = Properties.WATERLOGGED;
		bottomShape = Block.createColumnShape(16.0F, 0.0F, 8.0F);
		topShape = Block.createColumnShape(16.0F, 8.0F, 16.0F);
	}
}
