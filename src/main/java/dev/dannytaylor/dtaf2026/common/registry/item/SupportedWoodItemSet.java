package dev.dannytaylor.dtaf2026.common.registry.item;

import dev.dannytaylor.dtaf2026.common.registry.ItemRegistry;
import dev.dannytaylor.dtaf2026.common.registry.block.SupportedWoodBlockSet;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public class SupportedWoodItemSet {
	public final Item log;
	public final Item stripped_log;
	public final Item twice_stripped_log;
	public final Item wood;
	public final Item stripped_wood;
	public final Item twice_stripped_wood;
	public final Item planks;
	public final Item bark;

	private SupportedWoodItemSet(Item log, Item stripped_log, Item twice_stripped_log, Item wood, Item stripped_wood, Item twice_stripped_wood, Item planks, Item bark) {
		this.log = log;
		this.stripped_log = stripped_log;
		this.twice_stripped_log = twice_stripped_log;
		this.wood = wood;
		this.stripped_wood = stripped_wood;
		this.twice_stripped_wood = twice_stripped_wood;
		this.planks = planks;
		this.bark = bark;
	}

	public void addItemGroupEntries(FabricItemGroupEntries content) {
		content.add(this.log);
		content.add(this.stripped_log);
		content.add(this.twice_stripped_log);
		content.add(this.wood);
		content.add(this.stripped_wood);
		content.add(this.twice_stripped_wood);
		content.add(this.planks);
		content.add(this.bark);
	}

	public static Builder builder(SupportedWoodBlockSet blockSet) {
		return new Builder(blockSet);
	}

	public static class Builder {
		private final SupportedWoodBlockSet blockSet;

		public Builder(SupportedWoodBlockSet blockSet) {
			this.blockSet = blockSet;
		}

		public SupportedWoodItemSet build() {
			Item log = ItemRegistry.register(this.blockSet.id.withSuffixedPath("_log"), (settings) -> new SupportedLogBlockItem(this.blockSet.log, 0, settings), new BlockItem.Settings().useBlockPrefixedTranslationKey());
			Item stripped_log = ItemRegistry.register(this.blockSet.id.withPrefixedPath("stripped_").withSuffixedPath("_log"), (settings) -> new SupportedLogBlockItem(this.blockSet.log, 1, settings), new BlockItem.Settings().useBlockPrefixedTranslationKey());
			Item twice_stripped_log = ItemRegistry.register(this.blockSet.id.withPrefixedPath("twice_stripped_").withSuffixedPath("_log"), (settings) -> new SupportedLogBlockItem(this.blockSet.log, 2, settings), new BlockItem.Settings().useBlockPrefixedTranslationKey());

			Item wood = ItemRegistry.register(this.blockSet.id.withSuffixedPath("_wood"), (settings) -> new SupportedLogBlockItem(this.blockSet.wood, 0, settings), new BlockItem.Settings().useBlockPrefixedTranslationKey());
			Item stripped_wood = ItemRegistry.register(this.blockSet.id.withPrefixedPath("stripped_").withSuffixedPath("_wood"), (settings) -> new SupportedLogBlockItem(this.blockSet.wood, 1, settings), new BlockItem.Settings().useBlockPrefixedTranslationKey());
			Item twice_stripped_wood = ItemRegistry.register(this.blockSet.id.withPrefixedPath("twice_stripped_").withSuffixedPath("_wood"), (settings) -> new SupportedLogBlockItem(this.blockSet.wood, 2, settings), new BlockItem.Settings().useBlockPrefixedTranslationKey());

			Item planks = ItemRegistry.register(this.blockSet.id.withSuffixedPath("_planks"), (settings) -> new BlockItem(this.blockSet.planks, settings), new BlockItem.Settings().useBlockPrefixedTranslationKey());
			Item bark = ItemRegistry.register(this.blockSet.id.withSuffixedPath("_bark"), Item::new, new Item.Settings());
			return new SupportedWoodItemSet(log, stripped_log, twice_stripped_log, wood, stripped_wood, twice_stripped_wood, planks, bark);
		}
	}
}
