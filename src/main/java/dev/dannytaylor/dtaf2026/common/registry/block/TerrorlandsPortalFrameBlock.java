package dev.dannytaylor.dtaf2026.common.registry.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.predicate.block.BlockStatePredicate;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class TerrorlandsPortalFrameBlock extends TerrorlandsBasePortalFrameBlock {
	public static final MapCodec<TerrorlandsPortalFrameBlock> codec = createCodec(TerrorlandsPortalFrameBlock::new);
	public static final EnumProperty<Direction> facing;
	public static final BooleanProperty active;
	private static BlockPattern completedFrame;

	public MapCodec<? extends TerrorlandsPortalFrameBlock> getCodec() {
		return codec;
	}

	public TerrorlandsPortalFrameBlock(AbstractBlock.Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(facing, Direction.NORTH).with(active, false));
	}

	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(facing, ctx.getHorizontalPlayerFacing().getOpposite()).with(active, false);
	}

	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		return state.get(active) ? 15 : 0;
	}

	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(facing, rotation.rotate(state.get(facing)));
	}

	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(facing)));
	}

	public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(facing, active);
	}

	public static Block getAlwaysActiveBlock() {
		return BlockRegistry.terrorlandsPortalBaseFrame;
	}

	public static Block getInputBlock() {
		return BlockRegistry.terrorlandsPortalFrame;
	}

	public static BlockPattern getCompletedFramePattern() {
		if (completedFrame == null) {
			completedFrame = BlockPatternBuilder.start()
				.aisle("?vvv?", ">???<", ">???<", ">???<", "?^^^?")
				.where('?', CachedBlockPosition.matchesBlockState(BlockStatePredicate.ANY))
				.where('^', pos -> {
					BlockState state = pos.getBlockState();
					return state.isOf(getAlwaysActiveBlock()) ||
						(state.isOf(getInputBlock()) &&
							state.get(active) &&
							state.get(facing) == Direction.SOUTH);
				}).where('>', pos -> {
					BlockState state = pos.getBlockState();
					return state.isOf(getAlwaysActiveBlock()) ||
						(state.isOf(getInputBlock()) &&
							state.get(active) &&
							state.get(facing) == Direction.WEST);
				}).where('v', pos -> {
					BlockState state = pos.getBlockState();
					return state.isOf(getAlwaysActiveBlock()) ||
						(state.isOf(getInputBlock()) &&
							state.get(active) &&
							state.get(facing) == Direction.NORTH);
				}).where('<', pos -> {
					BlockState state = pos.getBlockState();
					return state.isOf(getAlwaysActiveBlock()) ||
						(state.isOf(getInputBlock()) &&
							state.get(active) &&
							state.get(facing) == Direction.EAST);
				}).build();
		}

		return completedFrame;
	}

	static {
		facing = HorizontalFacingBlock.FACING;
		active = BlockRegistry.Properties.active;
	}
}
