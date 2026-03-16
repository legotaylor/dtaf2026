/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.item;

import dev.dannytaylor.dtaf2026.common.registry.block.BlockRegistry;
import dev.dannytaylor.dtaf2026.common.registry.block.CreakingVariant;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;

public class CreakingVariantHeartBlockItem extends BlockItem {
	private final CreakingVariant variant;

	public CreakingVariantHeartBlockItem(Block block, CreakingVariant variant, Settings settings) {
		super(block, settings);
		this.variant = variant;
	}

	protected BlockState getPlacementState(ItemPlacementContext context) {
		BlockState state = super.getPlacementState(context);
		return state != null ? state.with(BlockRegistry.Properties.creakingVariant, this.variant) : null;
	}
}
