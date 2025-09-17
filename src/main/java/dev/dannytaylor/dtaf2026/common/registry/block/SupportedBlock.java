/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.block;

import com.mojang.serialization.MapCodec;
import dev.dannytaylor.dtaf2026.common.data.Data;
import net.minecraft.block.*;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

public class SupportedBlock extends Block {
	public static final MapCodec<? extends SupportedBlock> codec = createCodec(SupportedBlock::new);
	public static final int distance_max = Properties.DISTANCE_0_7_MAX;
	public static final IntProperty distance = Properties.DISTANCE_0_7;
	public static final BooleanProperty gravity = BooleanProperty.of(Data.idOf("gravity").toUnderscoreSeparatedString());

	public MapCodec<? extends SupportedBlock> getCodec() {
		return codec;
	}

	public SupportedBlock(AbstractBlock.Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(distance, distance_max).with(gravity, true));
	}

	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(distance, gravity);
	}

	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockState state = super.getPlacementState(ctx);
		return state != null ? state.with(distance, distance_max) : null;
	}

	protected void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		if (!world.isClient) {
			world.scheduleBlockTick(pos, this, 1);
		}
	}

	protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
		if (!world.isClient()) {
			tickView.scheduleBlockTick(pos, this, 1);
		}

		return state;
	}

	protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		BlockState newState = state.with(distance, calculateDistance(world, pos, isOf(), state.get(gravity)));
		if (newState.get(distance) == distance_max) {
			if (state.get(distance) == distance_max) FallingBlockEntity.spawnFromBlock(world, pos, newState);
			else world.breakBlock(pos, true);
		} else if (state != newState) world.setBlockState(pos, newState, 3);
	}

	public static int calculateDistance(BlockView world, BlockPos pos, IsOfBlock isOfBlock, boolean gravity) {
		if (!gravity) return 0;
		BlockPos downPos = pos.offset(Direction.DOWN);
		BlockState blockState = world.getBlockState(downPos);
		int i = distance_max;
		if (isOfBlock.call(blockState)) {
			i = blockState.get(distance);
		} else if (blockState.isSideSolidFullSquare(world, downPos, Direction.UP)) {
			return 0;
		}

		for (Direction direction : Direction.Type.HORIZONTAL) {
			BlockState blockState2 = world.getBlockState(pos.offset(direction));
			if (isOfBlock.call(blockState2)) {
				i = Math.min(i, blockState2.get(distance) + 1);
				if (i == 1) {
					break;
				}
			}
		}
		return Math.clamp(i, 0, 7);
	}

	public IsOfBlock isOf() {
		return (state -> state.isOf(this));
	}

	@FunctionalInterface
	public interface IsOfBlock {
		boolean call(BlockState state);
	}
}
