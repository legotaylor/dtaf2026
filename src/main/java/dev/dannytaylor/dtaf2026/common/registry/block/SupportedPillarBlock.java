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
import net.minecraft.block.PillarBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.Direction;

public class SupportedPillarBlock extends SupportedBlock {
	public static final MapCodec<SupportedPillarBlock> codec = createCodec(SupportedPillarBlock::new);
	public static final EnumProperty<Direction.Axis> axis = Properties.AXIS;

	public MapCodec<? extends SupportedPillarBlock> getCodec() {
		return codec;
	}

	public SupportedPillarBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(axis, Direction.Axis.Y));
	}

	protected BlockState rotate(BlockState state, BlockRotation rotation) {
		return PillarBlock.changeRotation(state, rotation);
	}

	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(axis);
	}

	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockState state = super.getPlacementState(ctx);
		return state != null ? state.with(axis, ctx.getSide().getAxis()) : null;
	}
}
