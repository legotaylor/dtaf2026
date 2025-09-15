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
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class SupportedLogBlock extends SupportedPillarBlock {
	public static final MapCodec<SupportedLogBlock> codec = createCodec(SupportedLogBlock::new);
	public static final IntProperty variant = IntProperty.of(Data.idOf("variant").toUnderscoreSeparatedString(), 0, 2);
	public static final BooleanProperty natural = BooleanProperty.of(Data.idOf("natural").toUnderscoreSeparatedString());

	public MapCodec<? extends SupportedLogBlock> getCodec() {
		return codec;
	}

	public SupportedLogBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(variant, 0).with(natural, false));
	}

	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(variant, natural);
	}

	public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
		if (!player.isCreative() && !(!tool.isEmpty() && Optional.ofNullable(tool.get(DataComponentTypes.ENCHANTMENTS)).map(ItemEnchantmentsComponent::getEnchantmentEntries).map(entries -> entries.stream().anyMatch(entry -> entry.getKey().isIn(TagRegistry.Enchantment.fullyBreaksSupportedLogs))).orElse(false)) && state.get(variant) < 2) world.setBlockState(pos, state.with(variant, state.get(variant) + 1), 3);
		super.afterBreak(world, player, pos, state, blockEntity, tool);
	}

	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockState state = super.getPlacementState(ctx);
		return state != null ? state.with(variant, 0).with(natural, false) : null;
	}

	public IsOfBlock isOf() {
		return (state -> state.isOf(BlockRegistry.maple_log));
	}
}
