/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.item;

import dev.dannytaylor.dtaf2026.common.data.Data;
import dev.dannytaylor.dtaf2026.common.registry.block.BlockRegistry;
import dev.dannytaylor.dtaf2026.common.registry.block.CreakingVariant;
import dev.dannytaylor.dtaf2026.common.registry.entity.EntityRegistry;
import dev.dannytaylor.dtaf2026.common.registry.item.component.RelicBundleContentsComponent;
import dev.dannytaylor.dtaf2026.common.registry.item.component.RelicComponent;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ConsumableComponents;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.ChestBoatEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ItemRegistry {
	public static final Item dirtBlock;
	public static final Item grassBlock;
	public static final Item dryGrassBlock;
	public static final Item stoneBlock;
	public static final Item stoneStairsBlock;
	public static final Item stoneSlabBlock;
	public static final Item cobblestoneBlock;
	public static final Item cobblestoneStairsBlock;
	public static final Item cobblestoneSlabBlock;
	public static final Item cobblestoneWallBlock;
	public static final Item deepslateBlock;
	public static final Item deepslateStairsBlock;
	public static final Item deepslateSlabBlock;
	public static final Item cobbledDeepslateBlock;
	public static final Item cobbledDeepslateStairsBlock;
	public static final Item cobbledDeepslateSlabBlock;
	public static final Item cobbledDeepslateWallBlock;
	public static final Item jasperOreBlock;
	public static final Item deepslateJasperOreBlock;
	public static final List<SupportedWoodItemSet> woodItemSets;
	public static final SupportedWoodItemSet maple;
	public static final SupportedWoodItemSet cerulean;
	public static final Item mapleCreakingHeart;
	public static final Item ceruleanCreakingHeart;
	public static final Item violet;
	public static final Item nightRelicBundle;
	public static final Item dayRelicBundle;
	public static final Item relicBundle;
	public static final Item jasper;
	public static final Item redEgg;
	public static final Item largeRedEgg;
	public static final Item grayEgg;
	public static final Item largeGrayEgg;
	public static final Item junglefowl;
	public static final Item cookedJunglefowl;
	public static final Item junglefowlSpawnEgg;
	public static final Item boarSpawnEgg;
	public static final Item fleeciferBossSpawnEgg;
	public static final Item fleeciferSpawnEgg;
	public static final Item eyeOfFleecifer;
	public static final Item fleeciferWool;

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

	@SuppressWarnings("deprecation")
	public static Item register(Block block, BiFunction<Block, Item.Settings, Item> factory, Item.Settings settings) {
		return register(keyOf(block.getRegistryEntry().registryKey()), (itemSettings) -> factory.apply(block, itemSettings), settings.useBlockPrefixedTranslationKey());
	}

	public static Item register(Identifier id, Function<Item.Settings, Item> factory, Item.Settings settings) {
		return register(keyOf(id), factory, settings);
	}

	public static Item register(String id, Function<Item.Settings, Item> factory, Item.Settings settings) {
		return register(Data.idOf(id), factory, settings);
	}

	public static Item registerRelic(Identifier id, Function<Item.Settings, Item> factory, Item.Settings settings, Identifier relicId) {
		return register(id, factory, relic(settings, relicId));
	}

	public static Item registerRelic(String id, Function<Item.Settings, Item> factory, Item.Settings settings) {
		Identifier identifier = Data.idOf(id);
		return registerRelic(identifier, factory, settings, identifier);
	}

	public static Item register(RegistryKey<Item> key, Function<Item.Settings, Item> factory, Item.Settings settings) {
		Item item = factory.apply(settings.registryKey(key));
		if (item instanceof BlockItem blockItem) blockItem.appendBlocks(Item.BLOCK_ITEMS, item);
		return Registry.register(Registries.ITEM, key, item);
	}

	public static void bootstrap() {
		ComponentTypeRegistry.bootstrap();
	}

	public static SupportedWoodItemSet register(SupportedWoodItemSet.Builder builder) {
		return register(builder, false, null, null);
	}

	public static SupportedWoodItemSet register(SupportedWoodItemSet.Builder builder, boolean raft, EntityType<BoatEntity> boatEntityEntityType, EntityType<ChestBoatEntity> chestBoatEntityEntityType) {
		SupportedWoodItemSet itemSet = builder.build(raft, boatEntityEntityType, chestBoatEntityEntityType);
		woodItemSets.add(itemSet);
		return itemSet;
	}

	public static Item.Settings relic(Item.Settings settings, Identifier relicId) {
		return settings.component(ComponentTypeRegistry.relic, new RelicComponent(relicId));
	}

	public static Item.Settings relicBundle(Item.Settings settings, RelicBundleContentsComponent relicBundleContentsComponent) {
		return settings.component(ComponentTypeRegistry.relicBundleContents, relicBundleContentsComponent).component(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true);
	}

	static {
		dirtBlock = register(BlockRegistry.dirt);
		grassBlock = register(BlockRegistry.grassBlock);
		dryGrassBlock = register(BlockRegistry.dryGrassBlock);
		stoneBlock = register(BlockRegistry.stone);
		stoneStairsBlock = register(BlockRegistry.stoneStairs);
		stoneSlabBlock = register(BlockRegistry.stoneSlab);
		cobblestoneBlock = register(BlockRegistry.cobblestone);
		cobblestoneStairsBlock = register(BlockRegistry.cobblestoneStairs);
		cobblestoneSlabBlock = register(BlockRegistry.cobblestoneSlab);
		cobblestoneWallBlock = register(BlockRegistry.cobblestoneWall);
		deepslateBlock = register(BlockRegistry.deepslate);
		deepslateStairsBlock = register(BlockRegistry.deepslateStairs);
		deepslateSlabBlock = register(BlockRegistry.deepslateSlab);
		cobbledDeepslateBlock = register(BlockRegistry.cobbledDeepslate);
		cobbledDeepslateStairsBlock = register(BlockRegistry.cobbledDeepslateStairs);
		cobbledDeepslateSlabBlock = register(BlockRegistry.cobbledDeepslateSlab);
		cobbledDeepslateWallBlock = register(BlockRegistry.cobbledDeepslateWall);
		jasperOreBlock = register(BlockRegistry.jasperOre);
		deepslateJasperOreBlock = register(BlockRegistry.deepslateJasperOre);
		woodItemSets = new ArrayList<>();
		maple = register(SupportedWoodItemSet.builder(BlockRegistry.maple), false, EntityRegistry.mapleBoat, EntityRegistry.mapleChestBoat);
		cerulean = register(SupportedWoodItemSet.builder(BlockRegistry.cerulean), false, EntityRegistry.ceruleanBoat, EntityRegistry.ceruleanChestBoat);
		mapleCreakingHeart = register("maple_creaking_heart", (settings) -> new CreakingVariantHeartBlockItem(Blocks.CREAKING_HEART, CreakingVariant.MAPLE, settings), new Item.Settings().useBlockPrefixedTranslationKey());
		ceruleanCreakingHeart = register("cerulean_creaking_heart", (settings) -> new CreakingVariantHeartBlockItem(Blocks.CREAKING_HEART, CreakingVariant.CERULEAN, settings), new Item.Settings().useBlockPrefixedTranslationKey());
		violet = register(BlockRegistry.violet);
		nightRelicBundle = register("night_relic_bundle", RelicBundleItem::new, relicBundle(new Item.Settings().maxCount(1), RelicBundleContentsComponent.empty).rarity(Rarity.RARE));
		dayRelicBundle = register("day_relic_bundle", RelicBundleItem::new, relicBundle(new Item.Settings().maxCount(1), RelicBundleContentsComponent.empty).rarity(Rarity.RARE));
		relicBundle = register("relic_bundle", RelicBundleItem::new, relicBundle(new Item.Settings().maxCount(1), RelicBundleContentsComponent.empty).rarity(Rarity.RARE));
		jasper = registerRelic("jasper", Item::new, new Item.Settings());
		redEgg = register(BlockRegistry.redEgg);
		largeRedEgg = register(BlockRegistry.largeRedEgg);
		grayEgg = register(BlockRegistry.grayEgg);
		largeGrayEgg = register(BlockRegistry.largeGrayEgg);
		junglefowl = register("junglefowl", Item::new, new Item.Settings().food(FoodComponents.CHICKEN, ConsumableComponents.RAW_CHICKEN));
		cookedJunglefowl = register("cooked_junglefowl", Item::new, new Item.Settings().food(FoodComponents.COOKED_CHICKEN));
		junglefowlSpawnEgg = register("junglefowl_spawn_egg", (settings) -> new SpawnEggItem(EntityRegistry.junglefowl, settings), new Item.Settings());
		boarSpawnEgg = register("boar_spawn_egg", (settings) -> new SpawnEggItem(EntityRegistry.boar, settings), new Item.Settings());
		fleeciferSpawnEgg = register("fleecifer_spawn_egg", (settings) -> new SpawnEggItem(EntityRegistry.fleecifer, settings), new Item.Settings());
		fleeciferBossSpawnEgg = register("fleecifer_boss_spawn_egg", (settings) -> new SpawnEggItem(EntityRegistry.fleeciferBoss, settings), new Item.Settings());
		eyeOfFleecifer = register("fleecifer_eye", FleeciferEyeItem::new, new Item.Settings());
		fleeciferWool = register("fleecifer_wool", Item::new, new Item.Settings());
	}
}
