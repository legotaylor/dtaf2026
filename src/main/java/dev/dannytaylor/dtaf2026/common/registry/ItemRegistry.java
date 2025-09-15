/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry;

import dev.dannytaylor.dtaf2026.common.data.Data;
import dev.dannytaylor.dtaf2026.common.registry.item.SupportedLogBlockItem;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import java.util.function.BiFunction;
import java.util.function.Function;

public class ItemRegistry {
	public static final Item maple_log;
	public static final Item stripped_maple_log;
	public static final Item twice_stripped_maple_log;
	public static final Item maple_wood;
	public static final Item stripped_maple_wood;
	public static final Item twice_stripped_maple_wood;
	public static final Item maple_bark;

	private static RegistryKey<Item> keyOf(String path) {
		return RegistryKey.of(RegistryKeys.ITEM, Data.idOf(path));
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

	public static Item register(String id, Function<Item.Settings, Item> factory, Item.Settings settings) {
		return register(keyOf(id), factory, settings);
	}

	public static Item register(RegistryKey<Item> key, Function<Item.Settings, Item> factory, Item.Settings settings) {
		Item item = factory.apply(settings.registryKey(key));
		if (item instanceof BlockItem blockItem) blockItem.appendBlocks(Item.BLOCK_ITEMS, item);
		return Registry.register(Registries.ITEM, key, item);
	}

	public static void bootstrap() {
	}

	static {
		maple_log = register("maple_log", (settings) -> new SupportedLogBlockItem(BlockRegistry.maple_log, 0, settings), new BlockItem.Settings().useBlockPrefixedTranslationKey());
		stripped_maple_log = register("stripped_maple_log", (settings) -> new SupportedLogBlockItem(BlockRegistry.maple_log, 1, settings), new BlockItem.Settings().useBlockPrefixedTranslationKey());
		twice_stripped_maple_log = register("twice_stripped_maple_log", (settings) -> new SupportedLogBlockItem(BlockRegistry.maple_log, 2, settings), new BlockItem.Settings().useBlockPrefixedTranslationKey());

		maple_wood = register("maple_wood", (settings) -> new SupportedLogBlockItem(BlockRegistry.maple_wood, 0, settings), new BlockItem.Settings().useBlockPrefixedTranslationKey());
		stripped_maple_wood = register("stripped_maple_wood", (settings) -> new SupportedLogBlockItem(BlockRegistry.maple_wood, 1, settings), new BlockItem.Settings().useBlockPrefixedTranslationKey());
		twice_stripped_maple_wood = register("twice_stripped_maple_wood", (settings) -> new SupportedLogBlockItem(BlockRegistry.maple_wood, 2, settings), new BlockItem.Settings().useBlockPrefixedTranslationKey());

		maple_bark = register("maple_bark", Item::new, new Item.Settings());
	}
}
