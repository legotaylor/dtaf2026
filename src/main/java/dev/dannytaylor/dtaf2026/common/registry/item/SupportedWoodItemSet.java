package dev.dannytaylor.dtaf2026.common.registry.item;

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

	private SupportedWoodItemSet(Blocks blocks, Blocks creativeBlocks, Item bark) {
		this.blocks = blocks;
		this.creativeBlocks = creativeBlocks;
		this.bark = bark;
	}

	public void addItemGroupEntries(FabricItemGroupEntries content, boolean creative) {
		if (!creative) {
			content.add(this.blocks.log);
			content.add(this.blocks.stripped_log);
			content.add(this.blocks.twice_stripped_log);
			content.add(this.blocks.wood);
			content.add(this.blocks.stripped_wood);
			content.add(this.blocks.twice_stripped_wood);
			content.add(this.blocks.planks);
			content.add(this.blocks.slab);
			content.add(this.bark);
		} else {
			content.add(this.creativeBlocks.log);
			content.add(this.creativeBlocks.stripped_log);
			content.add(this.creativeBlocks.twice_stripped_log);
			content.add(this.creativeBlocks.wood);
			content.add(this.creativeBlocks.stripped_wood);
			content.add(this.creativeBlocks.twice_stripped_wood);
			content.add(this.creativeBlocks.planks);
			content.add(this.creativeBlocks.slab);
		}
	}

	public static Builder builder(SupportedWoodBlockSet blockSet) {
		return new Builder(blockSet);
	}

	public static class Blocks {
		public final Item log;
		public final Item stripped_log;
		public final Item twice_stripped_log;
		public final Item wood;
		public final Item stripped_wood;
		public final Item twice_stripped_wood;
		public final Item planks;
		public final Item slab;

		private Blocks(Item log, Item stripped_log, Item twice_stripped_log, Item wood, Item stripped_wood, Item twice_stripped_wood, Item planks, Item slab) {
			this.log = log;
			this.stripped_log = stripped_log;
			this.twice_stripped_log = twice_stripped_log;
			this.wood = wood;
			this.stripped_wood = stripped_wood;
			this.twice_stripped_wood = twice_stripped_wood;
			this.planks = planks;
			this.slab = slab;
		}
	}

	public static class Builder {
		private final SupportedWoodBlockSet blockSet;

		public Builder(SupportedWoodBlockSet blockSet) {
			this.blockSet = blockSet;
		}

		private Blocks buildBlocks(boolean gravity) {
			Item log = ItemRegistry.register(this.blockSet.id.withPrefixedPath(!gravity ? "creative_" : "").withSuffixedPath("_log"), (settings) -> new SupportedLogBlockItem(this.blockSet.log, 0, gravity, settings), new BlockItem.Settings().useBlockPrefixedTranslationKey().rarity(!gravity ? Rarity.EPIC : Rarity.COMMON));
			Item stripped_log = ItemRegistry.register(this.blockSet.id.withPrefixedPath((!gravity ? "creative_" : "") + "stripped_").withSuffixedPath("_log"), (settings) -> new SupportedLogBlockItem(this.blockSet.log, 1, gravity, settings), new BlockItem.Settings().useBlockPrefixedTranslationKey().rarity(!gravity ? Rarity.EPIC : Rarity.COMMON));
			Item twice_stripped_log = ItemRegistry.register(this.blockSet.id.withPrefixedPath((!gravity ? "creative_" : "") + "twice_stripped_").withSuffixedPath("_log"), (settings) -> new SupportedLogBlockItem(this.blockSet.log, 2, gravity, settings), new BlockItem.Settings().useBlockPrefixedTranslationKey().rarity(!gravity ? Rarity.EPIC : Rarity.COMMON));

			Item wood = ItemRegistry.register(this.blockSet.id.withPrefixedPath(!gravity ? "creative_" : "").withSuffixedPath("_wood"), (settings) -> new SupportedLogBlockItem(this.blockSet.wood, 0, gravity, settings), new BlockItem.Settings().useBlockPrefixedTranslationKey().rarity(!gravity ? Rarity.EPIC : Rarity.COMMON));
			Item stripped_wood = ItemRegistry.register(this.blockSet.id.withPrefixedPath((!gravity ? "creative_" : "") + "stripped_").withSuffixedPath("_wood"), (settings) -> new SupportedLogBlockItem(this.blockSet.wood, 1, gravity, settings), new BlockItem.Settings().useBlockPrefixedTranslationKey().rarity(!gravity ? Rarity.EPIC : Rarity.COMMON));
			Item twice_stripped_wood = ItemRegistry.register(this.blockSet.id.withPrefixedPath((!gravity ? "creative_" : "") + "twice_stripped_").withSuffixedPath("_wood"), (settings) -> new SupportedLogBlockItem(this.blockSet.wood, 2, gravity, settings), new BlockItem.Settings().useBlockPrefixedTranslationKey().rarity(!gravity ? Rarity.EPIC : Rarity.COMMON));

			Item planks = ItemRegistry.register(this.blockSet.id.withPrefixedPath(!gravity ? "creative_" : "").withSuffixedPath("_planks"), (settings) -> new SupportedBlockItem(this.blockSet.planks, gravity, settings), new BlockItem.Settings().useBlockPrefixedTranslationKey().rarity(!gravity ? Rarity.EPIC : Rarity.COMMON));
			Item slab = ItemRegistry.register(this.blockSet.id.withPrefixedPath(!gravity ? "creative_" : "").withSuffixedPath("_slab"), (settings) -> new SupportedBlockItem(this.blockSet.slab, gravity, settings), new BlockItem.Settings().useBlockPrefixedTranslationKey().rarity(!gravity ? Rarity.EPIC : Rarity.COMMON));
			return new Blocks(log, stripped_log, twice_stripped_log, wood, stripped_wood, twice_stripped_wood, planks, slab);
		}

		public SupportedWoodItemSet build() {
			Item bark = ItemRegistry.register(this.blockSet.id.withSuffixedPath("_bark"), Item::new, new Item.Settings());
			Blocks creativeBlocks = buildBlocks(false);
			Blocks blocks = buildBlocks(true);
			return new SupportedWoodItemSet(blocks, creativeBlocks, bark);
		}
	}
}
