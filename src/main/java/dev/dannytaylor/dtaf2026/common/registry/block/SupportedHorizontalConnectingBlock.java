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
import net.minecraft.block.*;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import java.util.Map;
import java.util.function.Function;

public class SupportedHorizontalConnectingBlock extends SupportedBlock implements Waterloggable {
	public static final BooleanProperty north;
	public static final BooleanProperty east;
	public static final BooleanProperty south;
	public static final BooleanProperty west;
	public static final BooleanProperty waterlogged;
	public static final Map<Direction, BooleanProperty> FACING_PROPERTIES;
	public static final MapCodec<SupportedHorizontalConnectingBlock> codec = RecordCodecBuilder.mapCodec(
		instance -> instance.group(
				Codec.FLOAT.fieldOf("radius1").forGetter(block -> block.radius1),
				Codec.FLOAT.fieldOf("radius2").forGetter(block -> block.radius2),
				Codec.FLOAT.fieldOf("boundingHeight1").forGetter(block -> block.boundingHeight1),
				Codec.FLOAT.fieldOf("boundingHeight2").forGetter(block -> block.boundingHeight2),
				Codec.FLOAT.fieldOf("collisionHeight").forGetter(block -> block.collisionHeight),
				TagKey.codec(RegistryKeys.BLOCK).fieldOf("isOf").forGetter(block -> block.isOf),
				createSettingsCodec()
			)
			.apply(instance, SupportedHorizontalConnectingBlock::new)
	);

	static {
		north = ConnectingBlock.NORTH;
		east = ConnectingBlock.EAST;
		south = ConnectingBlock.SOUTH;
		west = ConnectingBlock.WEST;
		waterlogged = Properties.WATERLOGGED;
		FACING_PROPERTIES = ConnectingBlock.FACING_PROPERTIES.entrySet().stream().filter((entry) -> entry.getKey().getAxis().isHorizontal()).collect(Util.toMap());
	}

	public final float radius1;
	public final float radius2;
	public final float boundingHeight1;
	public final float boundingHeight2;
	public final float collisionHeight;
	private final Function<BlockState, VoxelShape> collisionShapeFunction;
	private final Function<BlockState, VoxelShape> outlineShapeFunction;

	public SupportedHorizontalConnectingBlock(float radius1, float radius2, float boundingHeight1, float boundingHeight2, float collisionHeight, TagKey<Block> isOf, AbstractBlock.Settings settings) {
		super(isOf, settings);
		this.collisionShapeFunction = this.createShapeFunction(radius1, collisionHeight, boundingHeight1, 0.0F, collisionHeight);
		this.outlineShapeFunction = this.createShapeFunction(radius1, radius2, boundingHeight1, 0.0F, boundingHeight2);

		this.radius1 = radius1;
		this.radius2 = radius2;
		this.boundingHeight1 = boundingHeight1;
		this.boundingHeight2 = boundingHeight2;
		this.collisionHeight = collisionHeight;
	}

	@Override
	public MapCodec<? extends SupportedHorizontalConnectingBlock> getCodec() {
		return codec;
	}

	protected Function<BlockState, VoxelShape> createShapeFunction(float radius1, float radius2, float height1, float offset2, float height2) {
		VoxelShape voxelShape = Block.createColumnShape(radius1, 0.0F, radius2);
		Map<Direction, VoxelShape> map = VoxelShapes.createHorizontalFacingShapeMap(Block.createCuboidZShape(height1, offset2, height2, 0.0F, 8.0F));
		return this.createShapeFunction((state) -> {
			VoxelShape voxelShape2 = voxelShape;

			for (Map.Entry<Direction, BooleanProperty> entry : FACING_PROPERTIES.entrySet()) {
				if (state.get(entry.getValue())) voxelShape2 = VoxelShapes.union(voxelShape2, map.get(entry.getKey()));
			}

			return voxelShape2;
		}, waterlogged);
	}

	protected boolean isTransparent(BlockState state) {
		return !(Boolean) state.get(waterlogged);
	}

	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return this.outlineShapeFunction.apply(state);
	}

	protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return this.collisionShapeFunction.apply(state);
	}

	protected FluidState getFluidState(BlockState state) {
		return state.get(waterlogged) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	protected boolean canPathfindThrough(BlockState state, NavigationType type) {
		return false;
	}

	protected BlockState rotate(BlockState state, BlockRotation rotation) {
		switch (rotation) {
			case CLOCKWISE_180 -> {
				return state.with(north, state.get(south)).with(east, state.get(west)).with(south, state.get(north)).with(west, state.get(east));
			}
			case COUNTERCLOCKWISE_90 -> {
				return state.with(north, state.get(east)).with(east, state.get(south)).with(south, state.get(west)).with(west, state.get(north));
			}
			case CLOCKWISE_90 -> {
				return state.with(north, state.get(west)).with(east, state.get(north)).with(south, state.get(east)).with(west, state.get(south));
			}
			default -> {
				return state;
			}
		}
	}

	protected BlockState mirror(BlockState state, BlockMirror mirror) {
		switch (mirror) {
			case LEFT_RIGHT -> {
				return state.with(north, state.get(south)).with(south, state.get(north));
			}
			case FRONT_BACK -> {
				return state.with(east, state.get(west)).with(west, state.get(east));
			}
			default -> {
				return super.mirror(state, mirror);
			}
		}
	}
}
