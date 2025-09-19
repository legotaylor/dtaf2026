/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.item;

import dev.dannytaylor.dtaf2026.common.registry.BlockRegistry;
import dev.dannytaylor.dtaf2026.common.registry.ItemRegistry;
import dev.dannytaylor.dtaf2026.common.registry.block.SupportedWoodBlockSet;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Rarity;

public class SupportedWoodItemSet {
	public final Blocks blocks;
	public final Blocks creativeBlocks;
	public final Item bark;
	public final Item sapling;

	private SupportedWoodItemSet(Blocks blocks, Blocks creativeBlocks, Item bark, Item sapling) {
		this.blocks = blocks;
		this.creativeBlocks = creativeBlocks;
		this.bark = bark;
		this.sapling = sapling;
	}

	public void addItemGroupEntries(FabricItemGroupEntries content, boolean creative) {
		if (!creative) {
			content.add(this.blocks.log.zero);
			content.add(this.blocks.log.one);
			content.add(this.blocks.log.two);
			content.add(this.blocks.wood.zero);
			content.add(this.blocks.wood.one);
			content.add(this.blocks.wood.two);
			content.add(this.blocks.planks);
			content.add(this.blocks.slab);
			content.add(this.blocks.leaves);
			content.add(this.bark);
			content.add(this.sapling);
		} else {
			content.add(this.creativeBlocks.log.zero);
			content.add(this.creativeBlocks.log.one);
			content.add(this.creativeBlocks.log.two);
			content.add(this.creativeBlocks.wood.zero);
			content.add(this.creativeBlocks.wood.one);
			content.add(this.creativeBlocks.wood.two);
			content.add(this.creativeBlocks.planks);
			content.add(this.creativeBlocks.slab);
			content.add(this.creativeBlocks.leaves);
		}
	}

	public static Builder builder(SupportedWoodBlockSet blockSet) {
		return new Builder(blockSet);
	}

	public static class Blocks {
		public final WoodLog log;
		public final WoodLog wood;
		public final Item planks;
		public final Item slab;
		public final Item leaves;

		private Blocks(WoodLog log, WoodLog wood, Item planks, Item slab, Item leaves) {
			this.log = log;
			this.wood = wood;
			this.planks = planks;
			this.slab = slab;
			this.leaves = leaves;
		}

		public static class WoodLog {
			private final Item zero;
			private final Item one;
			private final Item two;

			private WoodLog(Item zero, Item one, Item two) {
				this.zero = zero;
				this.one = one;
				this.two = two;
			}
		}
	}

	public static class Builder {
		private final SupportedWoodBlockSet blockSet;

		public Builder(SupportedWoodBlockSet blockSet) {
			this.blockSet = blockSet;
		}

		private Blocks buildBlocks(boolean gravity) {
			Item log = ItemRegistry.register(this.blockSet.id.withPrefixedPath(BlockRegistry.getLogIdPrefix(0, !gravity)).withSuffixedPath("_log"), (settings) -> new SupportedLogBlockItem(this.blockSet.log, 0, gravity, settings), new BlockItem.Settings().useBlockPrefixedTranslationKey().rarity(!gravity ? Rarity.EPIC : Rarity.COMMON));
			Item stripped_log = ItemRegistry.register(this.blockSet.id.withPrefixedPath(BlockRegistry.getLogIdPrefix(1, !gravity)).withSuffixedPath("_log"), (settings) -> new SupportedLogBlockItem(this.blockSet.log, 1, gravity, settings), new BlockItem.Settings().useBlockPrefixedTranslationKey().rarity(!gravity ? Rarity.EPIC : Rarity.COMMON));
			Item twice_stripped_log = ItemRegistry.register(this.blockSet.id.withPrefixedPath(BlockRegistry.getLogIdPrefix(2, !gravity)).withSuffixedPath("_log"), (settings) -> new SupportedLogBlockItem(this.blockSet.log, 2, gravity, settings), new BlockItem.Settings().useBlockPrefixedTranslationKey().rarity(!gravity ? Rarity.EPIC : Rarity.COMMON));

			Item wood = ItemRegistry.register(this.blockSet.id.withPrefixedPath(BlockRegistry.getLogIdPrefix(0, !gravity)).withSuffixedPath("_wood"), (settings) -> new SupportedLogBlockItem(this.blockSet.wood, 0, gravity, settings), new BlockItem.Settings().useBlockPrefixedTranslationKey().rarity(!gravity ? Rarity.EPIC : Rarity.COMMON));
			Item stripped_wood = ItemRegistry.register(this.blockSet.id.withPrefixedPath(BlockRegistry.getLogIdPrefix(1, !gravity)).withSuffixedPath("_wood"), (settings) -> new SupportedLogBlockItem(this.blockSet.wood, 1, gravity, settings), new BlockItem.Settings().useBlockPrefixedTranslationKey().rarity(!gravity ? Rarity.EPIC : Rarity.COMMON));
			Item twice_stripped_wood = ItemRegistry.register(this.blockSet.id.withPrefixedPath(BlockRegistry.getLogIdPrefix(2, !gravity)).withSuffixedPath("_wood"), (settings) -> new SupportedLogBlockItem(this.blockSet.wood, 2, gravity, settings), new BlockItem.Settings().useBlockPrefixedTranslationKey().rarity(!gravity ? Rarity.EPIC : Rarity.COMMON));

			Item planks = ItemRegistry.register(this.blockSet.id.withPrefixedPath(BlockRegistry.getIdPrefix(!gravity)).withSuffixedPath("_planks"), (settings) -> new SupportedBlockItem(this.blockSet.planks, gravity, settings), new BlockItem.Settings().useBlockPrefixedTranslationKey().rarity(!gravity ? Rarity.EPIC : Rarity.COMMON));
			Item slab = ItemRegistry.register(this.blockSet.id.withPrefixedPath(BlockRegistry.getIdPrefix(!gravity)).withSuffixedPath("_slab"), (settings) -> new SupportedBlockItem(this.blockSet.slab, gravity, settings), new BlockItem.Settings().useBlockPrefixedTranslationKey().rarity(!gravity ? Rarity.EPIC : Rarity.COMMON));

			Item leaves = ItemRegistry.register(this.blockSet.id.withPrefixedPath(BlockRegistry.getIdPrefix(!gravity)).withSuffixedPath("_leaves"), (settings) -> new SupportedBlockItem(this.blockSet.leaves, gravity, settings), new BlockItem.Settings().useBlockPrefixedTranslationKey().rarity(!gravity ? Rarity.EPIC : Rarity.COMMON));

			return new Blocks(new Blocks.WoodLog(log, stripped_log, twice_stripped_log), new Blocks.WoodLog(wood, stripped_wood, twice_stripped_wood), planks, slab, leaves);
		}

		public SupportedWoodItemSet build() {
			Item bark = ItemRegistry.register(this.blockSet.id.withSuffixedPath("_bark"), Item::new, new Item.Settings());
			Item sapling = ItemRegistry.register(this.blockSet.id.withSuffixedPath("_sapling"), (settings) -> new BlockItem(this.blockSet.sapling, settings), new BlockItem.Settings().useBlockPrefixedTranslationKey());
			Blocks creativeBlocks = buildBlocks(false);
			Blocks blocks = buildBlocks(true);
			return new SupportedWoodItemSet(blocks, creativeBlocks, bark, sapling);
		}
	}
}
