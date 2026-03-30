/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.dannytaylor.dtaf2026.common.data.Data;
import dev.dannytaylor.dtaf2026.common.registry.tagkey.TagRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SideShapeType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

public class SupportedBlock extends Block {
	public static final MapCodec<SupportedBlock> codec = RecordCodecBuilder.mapCodec(
		instance -> instance.group(
				TagKey.codec(RegistryKeys.BLOCK).fieldOf("isOf").forGetter(block -> block.isOf),
				createSettingsCodec()
			)
			.apply(instance, SupportedBlock::new)
	);
	public static final int distance_max = Properties.DISTANCE_0_7_MAX;
	public static final IntProperty distance = Properties.DISTANCE_0_7;
	public static final BooleanProperty gravity = BooleanProperty.of(Data.idOf("gravity").toUnderscoreSeparatedString());
	public final TagKey<Block> isOf;

	public MapCodec<? extends SupportedBlock> getCodec() {
		return codec;
	}

	public SupportedBlock(TagKey<Block> isOf, AbstractBlock.Settings settings) {
		super(settings);
		this.isOf = isOf;
		this.setDefaultState(this.stateManager.getDefaultState().with(distance, distance_max).with(gravity, true));
	}

	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(distance, gravity);
	}

	public static int calculateDistance(BlockView world, BlockPos pos, IsOfBlock isOfBlock, boolean gravity) {
		if (!gravity) return 0;
		BlockPos downPos = pos.offset(Direction.DOWN);
		BlockState downState = world.getBlockState(downPos);
		int distance = downState.isIn(TagRegistry.Block.alwaysSupportsSupportedBlocks) ? 0 : distance_max;
		if (isOfBlock.call(downState) && !downState.isIn(TagRegistry.Block.alwaysSupportsSupportedBlocks)) {
			distance = downState.isSideSolid(world, downPos, Direction.UP, SideShapeType.CENTER) ? downState.get(SupportedBlock.distance) : distance_max;
		} else if (!downState.isReplaceable()) return 0;

		for (Direction direction : Direction.Type.HORIZONTAL) {
			BlockPos offsetPos = pos.offset(direction);
			BlockState offsetState = world.getBlockState(offsetPos);
			if (offsetState.isIn(TagRegistry.Block.alwaysSupportsSupportedBlocks)) distance = 1;
			else if (isOfBlock.call(offsetState))
				distance = Math.min(distance, offsetState.get(SupportedBlock.distance) + 1);
			if (distance <= 1) break;
		}
		return Math.clamp(distance, 0, 7);
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
		BlockState blockState = state.with(distance, calculateDistance(world, pos, isOf(), state.get(gravity)));
		if (blockState.get(distance) == distance_max) {
			if (state.get(distance) == distance_max) FallingBlockEntity.spawnFromBlock(world, pos, getFallingBlockState(blockState));
			else world.breakBlock(pos, true);
		} else if (state != blockState) world.setBlockState(pos, blockState, 3);
	}

	public BlockState getFallingBlockState(BlockState state) {
		return state;
	}

	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockState state = super.getPlacementState(ctx);
		return state != null ? state.with(distance, distance_max) : null;
	}

	public IsOfBlock isOf() {
		return (state -> this.isOf != null ? state.isIn(this.isOf) : state.isOf(this));
	}

	@FunctionalInterface
	public interface IsOfBlock {
		boolean call(BlockState state);
	}

	protected ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state, boolean includeData) {
		return new ItemStack(getItem(this.getRegistryEntry().registryKey(), !state.get(gravity)));
	}

	public static Item getItem(RegistryKey<Block> block, boolean creative) {
		return Registries.ITEM.get(getId(block.getValue(), creative));
	}

	public static Identifier getId(Identifier id, boolean creative) {
		// We assume all SupportedLogBlock's use the same naming system.
		// If you're making an addon mod, I would suggest using the same naming system.
		return id.withPrefixedPath(BlockRegistry.getIdPrefix(creative));
	}
}
