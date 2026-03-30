package dev.dannytaylor.dtaf2026.common.registry.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.predicate.block.BlockStatePredicate;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class TerrorlandsBasePortalFrameBlock extends Block {
	public static final MapCodec<TerrorlandsBasePortalFrameBlock> codec = createCodec(TerrorlandsBasePortalFrameBlock::new);
	public static final VoxelShape frameShape;

	public MapCodec<? extends TerrorlandsBasePortalFrameBlock> getCodec() {
		return codec;
	}

	public TerrorlandsBasePortalFrameBlock(Settings settings) {
		super(settings);
	}

	public boolean hasSidedTransparency(BlockState state) {
		return true;
	}

	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return frameShape;
	}

	public boolean canPathfindThrough(BlockState state, NavigationType type) {
		return false;
	}

	static {
		frameShape = Block.createColumnShape(16.0F, 0.0F, 13.0F);
	}
}
