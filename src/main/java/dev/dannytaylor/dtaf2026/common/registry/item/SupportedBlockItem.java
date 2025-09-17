/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.item;

import dev.dannytaylor.dtaf2026.common.registry.block.SupportedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;

public class SupportedBlockItem extends BlockItem {
	private final boolean gravity;

	public SupportedBlockItem(SupportedBlock block, Settings settings) {
		this(block, true, settings);
	}

	public SupportedBlockItem(SupportedBlock block, boolean gravity, Settings settings) {
		super(block, settings);
		this.gravity = gravity;
	}

	protected BlockState getPlacementState(ItemPlacementContext context) {
		BlockState state = super.getPlacementState(context);
		return state != null ? state.with(SupportedBlock.gravity, this.gravity) : null;
	}
}
