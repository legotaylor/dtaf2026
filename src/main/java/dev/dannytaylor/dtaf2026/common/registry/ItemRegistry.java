/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry;

import dev.dannytaylor.dtaf2026.common.data.Data;
import dev.dannytaylor.dtaf2026.common.registry.item.SupportedWoodItemSet;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ItemRegistry {
	public static final List<SupportedWoodItemSet> woodItemSets;
	public static final SupportedWoodItemSet maple;

	private static RegistryKey<Item> keyOf(Identifier path) {
		return RegistryKey.of(RegistryKeys.ITEM, path);
	}

	private static RegistryKey<Item> keyOf(RegistryKey<Block> blockKey) {
		return RegistryKey.of(RegistryKeys.ITEM, blockKey.getValue());
	}

	public static Item register(Block block) {
		return register(block, BlockItem::new);
	}

	public static Item register(Block block, BiFunction<Block, Item.Settings, Item> factory) {
		return register(block, factory, new Item.Settings());
	}

	public static Item register(Block block, BiFunction<Block, Item.Settings, Item> factory, Item.Settings settings) {
		return register(keyOf(block.getRegistryEntry().registryKey()), (Function)((itemSettings) -> (Item)factory.apply(block, (Item.Settings) itemSettings)), settings.useBlockPrefixedTranslationKey());
	}

	public static Item register(Identifier id, Function<Item.Settings, Item> factory, Item.Settings settings) {
		return register(keyOf(id), factory, settings);
	}

	public static Item register(String id, Function<Item.Settings, Item> factory, Item.Settings settings) {
		return register(Data.idOf(id), factory, settings);
	}

	public static Item register(RegistryKey<Item> key, Function<Item.Settings, Item> factory, Item.Settings settings) {
		Item item = factory.apply(settings.registryKey(key));
		if (item instanceof BlockItem blockItem) blockItem.appendBlocks(Item.BLOCK_ITEMS, item);
		return Registry.register(Registries.ITEM, key, item);
	}

	public static void bootstrap() {
	}

	public static SupportedWoodItemSet register(SupportedWoodItemSet.Builder builder) {
		SupportedWoodItemSet itemSet = builder.build();
		woodItemSets.add(itemSet);
		return itemSet;
	}

	static {
		woodItemSets = new ArrayList<>();
		maple = register(SupportedWoodItemSet.builder(BlockRegistry.maple));
	}
}
