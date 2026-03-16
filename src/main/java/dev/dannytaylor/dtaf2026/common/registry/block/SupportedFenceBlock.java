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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.LeadItem;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

import java.util.function.Function;

public class SupportedFenceBlock extends SupportedHorizontalConnectingBlock {
	public static final MapCodec<SupportedFenceBlock> codec = RecordCodecBuilder.mapCodec(
		instance -> instance.group(
				TagKey.codec(RegistryKeys.BLOCK).fieldOf("isOf").forGetter(block -> block.isOf),
				createSettingsCodec()
			)
			.apply(instance, SupportedFenceBlock::new)
	);
	private final Function<BlockState, VoxelShape> cullingShapeFunction;

	public SupportedFenceBlock(TagKey<Block> idOf, AbstractBlock.Settings settings) {
		super(4.0F, 16.0F, 4.0F, 16.0F, 24.0F, idOf, settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(north, false).with(east, false).with(south, false).with(west, false).with(waterlogged, false));
		this.cullingShapeFunction = this.createShapeFunction(4.0F, 16.0F, 2.0F, 6.0F, 15.0F);
	}

	public MapCodec<? extends SupportedFenceBlock> getCodec() {
		return codec;
	}

	protected VoxelShape getCullingShape(BlockState state) {
		return this.cullingShapeFunction.apply(state);
	}

	protected VoxelShape getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return this.getOutlineShape(state, world, pos, context);
	}

	public boolean canConnect(BlockState state, boolean neighborIsFullSquare, Direction dir) {
		Block block = state.getBlock();
		boolean bl = this.canConnectToFence(state);
		boolean bl2 = block instanceof FenceGateBlock && FenceGateBlock.canWallConnect(state, dir);
		boolean bl3 = block instanceof SupportedFenceGateBlock && SupportedFenceGateBlock.canWallConnect(state, dir);
		return !cannotConnect(state) && neighborIsFullSquare || bl || bl2 || bl3;
	}

	private boolean canConnectToFence(BlockState state) {
		return state.isIn(BlockTags.FENCES) && state.isIn(BlockTags.WOODEN_FENCES) == this.getDefaultState().isIn(BlockTags.WOODEN_FENCES);
	}

	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		return !world.isClient() ? LeadItem.attachHeldMobsToBlock(player, world, pos) : ActionResult.PASS;
	}

	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockState state = super.getPlacementState(ctx);
		if (state != null) {
			BlockView blockView = ctx.getWorld();
			BlockPos blockPos = ctx.getBlockPos();
			FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
			BlockPos blockPos2 = blockPos.north();
			BlockPos blockPos3 = blockPos.east();
			BlockPos blockPos4 = blockPos.south();
			BlockPos blockPos5 = blockPos.west();
			BlockState blockState = blockView.getBlockState(blockPos2);
			BlockState blockState2 = blockView.getBlockState(blockPos3);
			BlockState blockState3 = blockView.getBlockState(blockPos4);
			BlockState blockState4 = blockView.getBlockState(blockPos5);
			return state.with(north, this.canConnect(blockState, blockState.isSideSolidFullSquare(blockView, blockPos2, Direction.SOUTH), Direction.SOUTH)).with(east, this.canConnect(blockState2, blockState2.isSideSolidFullSquare(blockView, blockPos3, Direction.WEST), Direction.WEST)).with(south, this.canConnect(blockState3, blockState3.isSideSolidFullSquare(blockView, blockPos4, Direction.NORTH), Direction.NORTH)).with(west, this.canConnect(blockState4, blockState4.isSideSolidFullSquare(blockView, blockPos5, Direction.EAST), Direction.EAST)).with(waterlogged, fluidState.getFluid() == Fluids.WATER);
		}
		return null;
	}

	protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
		if (state.get(waterlogged)) tickView.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		return super.getStateForNeighborUpdate(direction.getAxis().isHorizontal() ? state.with(FACING_PROPERTIES.get(direction), this.canConnect(neighborState, neighborState.isSideSolidFullSquare(world, neighborPos, direction.getOpposite()), direction.getOpposite())) : state, world, tickView, pos, direction, neighborPos, neighborState, random);
	}

	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(north, east, west, south, waterlogged);
	}
}
