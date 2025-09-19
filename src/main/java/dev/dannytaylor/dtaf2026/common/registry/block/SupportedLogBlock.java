/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.block;

import com.mojang.serialization.MapCodec;
import dev.dannytaylor.dtaf2026.common.data.Data;
import dev.dannytaylor.dtaf2026.common.registry.BlockRegistry;
import dev.dannytaylor.dtaf2026.common.registry.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class SupportedLogBlock extends SupportedPillarBlock {
	public static final MapCodec<? extends SupportedLogBlock> codec = createCodec(SupportedLogBlock::new);
	public static final IntProperty variant = IntProperty.of(Data.idOf("variant").toUnderscoreSeparatedString(), 0, 2);

	public MapCodec<? extends SupportedLogBlock> getCodec() {
		return codec;
	}

	public SupportedLogBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(variant, 0));
	}

	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(variant);
	}

	public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
		if (!player.isCreative() && !(!tool.isEmpty() && Optional.ofNullable(tool.get(DataComponentTypes.ENCHANTMENTS)).map(ItemEnchantmentsComponent::getEnchantmentEntries).map(entries -> entries.stream().anyMatch(entry -> entry.getKey().isIn(TagRegistry.Enchantment.fullyBreaksSupportedLogs))).orElse(false)) && state.get(variant) < 2) world.setBlockState(pos, state.with(variant, state.get(variant) + 1), 3);
		super.afterBreak(world, player, pos, state, blockEntity, tool);
	}

	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return super.getPlacementState(ctx).with(variant, 0);
	}

	public IsOfBlock isOf() {
		return (state -> state.isIn(TagRegistry.Block.supportedLogs));
	}

	protected ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state, boolean includeData) {
		return new ItemStack(getLogItem(this.getRegistryEntry().registryKey(), state.get(SupportedLogBlock.variant), !state.get(SupportedLogBlock.gravity)));
	}

	public static Item getLogItem(RegistryKey<Block> block, int variant, boolean creative) {
		return Registries.ITEM.get(getLogId(block.getValue(), variant, creative));
	}

	public static Identifier getLogId(Identifier id, int variant, boolean creative) {
		// We assume all SupportedLogBlock's use the same naming system.
		// If you're making an addon mod, I would suggest using the same naming system.
		return id.withPrefixedPath(BlockRegistry.getLogIdPrefix(variant, creative));
	}
}
