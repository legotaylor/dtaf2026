/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.item;

import dev.dannytaylor.dtaf2026.common.registry.block.SupportedLogBlock;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;

public class SupportedLogBlockItem extends BlockItem {
	private final int variant;
	public SupportedLogBlockItem(SupportedLogBlock block, int variant, Settings settings) {
		super(block, settings);
		this.variant = Math.clamp(variant, 0, 2);
	}

	protected BlockState getPlacementState(ItemPlacementContext context) {
		BlockState state = super.getPlacementState(context);
		return state != null ? state.with(SupportedLogBlock.variant, this.variant) : null;
	}
}
