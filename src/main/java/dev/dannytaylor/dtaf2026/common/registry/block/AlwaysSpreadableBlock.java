/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.light.ChunkLightProvider;

public abstract class AlwaysSpreadableBlock extends net.minecraft.block.SpreadableBlock {
	private final Block baseBlock;

	protected AlwaysSpreadableBlock(AbstractBlock.Settings settings, Block baseBlock) {
		super(settings);
		this.baseBlock = baseBlock;
	}

	private static boolean canSurvive(BlockState state, WorldView world, BlockPos pos) {
		BlockPos blockPos = pos.up();
		BlockState blockState = world.getBlockState(blockPos);
		if (blockState.isOf(Blocks.SNOW) && blockState.get(SnowBlock.LAYERS) == 1) {
			return true;
		} else if (blockState.getFluidState().getLevel() == 8) {
			return false;
		} else {
			int i = ChunkLightProvider.getRealisticOpacity(state, blockState, Direction.UP, blockState.getOpacity());
			return i < 15;
		}
	}

	public static boolean canSpread(BlockState state, WorldView world, BlockPos pos) {
		BlockPos blockPos = pos.up();
		return canSurvive(state, world, pos) && !world.getFluidState(blockPos).isIn(FluidTags.WATER);
	}

	public abstract MapCodec<? extends AlwaysSpreadableBlock> getCodec();

	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (!canSurvive(state, world, pos)) {
			if (this.baseBlock != null) world.setBlockState(pos, this.baseBlock.getDefaultState());
		} else {
			BlockState blockState = this.getDefaultState();
			for (int i = 0; i < 4; ++i) {
				BlockPos blockPos = pos.add(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
				if (world.getBlockState(blockPos).isOf(this.baseBlock) && canSpread(blockState, world, blockPos)) {
					world.setBlockState(blockPos, blockState.with(SNOWY, isSnow(world.getBlockState(blockPos.up()))));
				}
			}
		}
	}
}
