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
import net.minecraft.item.*;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.Util;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ItemRegistry {
	public static final List<Item> items;
	public static final List<Item> creativeItems;
	public static final List<SupportedWoodItemSet> woodItemSets;

	public static final Item grassBlock;
	public static final Item dryGrassBlock;
	public static final Item dirtBlock;

	public static final Item stoneBlock;
	public static final Item stoneStairsBlock;
	public static final Item stoneSlabBlock;

	public static final Item cobblestoneBlock;
	public static final Item cobblestoneStairsBlock;
	public static final Item cobblestoneSlabBlock;
	public static final Item cobblestoneWallBlock;

	public static final Item extractor;

	public static final Item aerostoneOreBlock;
	public static final Item amberOreBlock;
	public static final Item colosineOreBlock;
	public static final Item jasperOreBlock;
	public static final Item quariteOreBlock;
	public static final Item rubyOreBlock;

	public static final Item deepslateBlock;
	public static final Item deepslateStairsBlock;
	public static final Item deepslateSlabBlock;

	public static final Item cobbledDeepslateBlock;
	public static final Item cobbledDeepslateStairsBlock;
	public static final Item cobbledDeepslateSlabBlock;
	public static final Item cobbledDeepslateWallBlock;

	public static final Item deepslateAerostoneOreBlock;
	public static final Item deepslateAmberOreBlock;
	public static final Item deepslateColosineOreBlock;
	public static final Item deepslateJasperOreBlock;
	public static final Item deepslateQuariteOreBlock;
	public static final Item deepslateRubyOreBlock;

	public static final Item terrorlandsPortalBaseFrame;
	public static final Item terrorlandsPortalFrame;

	public static final SupportedWoodItemSet maple;
	public static final Item mapleCreakingHeart;

	public static final SupportedWoodItemSet cerulean;
	public static final Item ceruleanCreakingHeart;

	public static final Item violet;

	public static final Item nightRelicBundle;
	public static final Item dayRelicBundle;
	public static final Item relicBundle;

	public static final Item aerostone;
	public static final Item amber;
	public static final Item colosine;
	public static final Item jasper;
	public static final Item quarite;
	public static final Item ruby;

	public static final Item aerostoneTorch;
	public static final Item aerostoneLantern;

	public static final Item jasperHelmet;
	public static final Item jasperChestplate;
	public static final Item jasperLeggings;
	public static final Item jasperBoots;

	public static final Item quaritePickaxe;

	public static final Item rubySword;

	public static final Item relicDust;

	public static final Item terrorlandsCompass;

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

	public static final Item fleeciferWool;
	public static final Item eyeOfFleecifer;
	public static final Item relicBundleUpgradeSmithingTemplate;

	private static RegistryKey<Item> keyOf(Identifier path) {
		return RegistryKey.of(RegistryKeys.ITEM, path);
	}

	private static RegistryKey<Item> keyOf(RegistryKey<Block> blockKey) {
		return RegistryKey.of(RegistryKeys.ITEM, blockKey.getValue());
	}

	static {
		items = new ArrayList<>();
		creativeItems = new ArrayList<>();
		woodItemSets = new ArrayList<>();

		grassBlock = register(BlockRegistry.grassBlock);
		dryGrassBlock = register(BlockRegistry.dryGrassBlock);
		dirtBlock = register(BlockRegistry.dirt);

		stoneBlock = register(BlockRegistry.stone);
		stoneStairsBlock = register(BlockRegistry.stoneStairs);
		stoneSlabBlock = register(BlockRegistry.stoneSlab);

		cobblestoneBlock = register(BlockRegistry.cobblestone);
		cobblestoneStairsBlock = register(BlockRegistry.cobblestoneStairs);
		cobblestoneSlabBlock = register(BlockRegistry.cobblestoneSlab);
		cobblestoneWallBlock = register(BlockRegistry.cobblestoneWall);

		extractor = register(BlockRegistry.extractor);

		aerostoneOreBlock = register(BlockRegistry.aerostoneOre);
		amberOreBlock = register(BlockRegistry.amberOre);
		colosineOreBlock = register(BlockRegistry.colosineOre);
		jasperOreBlock = register(BlockRegistry.jasperOre);
		quariteOreBlock = register(BlockRegistry.quariteOre);
		rubyOreBlock = register(BlockRegistry.rubyOre);

		deepslateBlock = register(BlockRegistry.deepslate);
		deepslateStairsBlock = register(BlockRegistry.deepslateStairs);
		deepslateSlabBlock = register(BlockRegistry.deepslateSlab);

		cobbledDeepslateBlock = register(BlockRegistry.cobbledDeepslate);
		cobbledDeepslateStairsBlock = register(BlockRegistry.cobbledDeepslateStairs);
		cobbledDeepslateSlabBlock = register(BlockRegistry.cobbledDeepslateSlab);
		cobbledDeepslateWallBlock = register(BlockRegistry.cobbledDeepslateWall);

		deepslateAerostoneOreBlock = register(BlockRegistry.deepslateAerostoneOre);
		deepslateAmberOreBlock = register(BlockRegistry.deepslateAmberOre);
		deepslateColosineOreBlock = register(BlockRegistry.deepslateColosineOre);
		deepslateJasperOreBlock = register(BlockRegistry.deepslateJasperOre);
		deepslateQuariteOreBlock = register(BlockRegistry.deepslateQuariteOre);
		deepslateRubyOreBlock = register(BlockRegistry.deepslateRubyOre);

		terrorlandsPortalBaseFrame = register(BlockRegistry.terrorlandsPortalBaseFrame);
		terrorlandsPortalFrame = register(BlockRegistry.terrorlandsPortalFrame);

		maple = register(SupportedWoodItemSet.builder(BlockRegistry.maple), false, EntityRegistry.mapleBoat, EntityRegistry.mapleChestBoat);
		mapleCreakingHeart = register("maple_creaking_heart", (settings) -> new CreakingVariantHeartBlockItem(Blocks.CREAKING_HEART, CreakingVariant.MAPLE, settings), new Item.Settings().useBlockPrefixedTranslationKey());

		cerulean = register(SupportedWoodItemSet.builder(BlockRegistry.cerulean), false, EntityRegistry.ceruleanBoat, EntityRegistry.ceruleanChestBoat);
		ceruleanCreakingHeart = register("cerulean_creaking_heart", (settings) -> new CreakingVariantHeartBlockItem(Blocks.CREAKING_HEART, CreakingVariant.CERULEAN, settings), new Item.Settings().useBlockPrefixedTranslationKey());

		violet = register(BlockRegistry.violet);

		nightRelicBundle = register("night_relic_bundle", RelicBundleItem::new, relicBundle(new Item.Settings().maxCount(1), RelicBundleContentsComponent.empty).rarity(Rarity.RARE));
		dayRelicBundle = register("day_relic_bundle", RelicBundleItem::new, relicBundle(new Item.Settings().maxCount(1), RelicBundleContentsComponent.empty).rarity(Rarity.RARE));
		relicBundle = register("relic_bundle", RelicBundleItem::new, relicBundle(new Item.Settings().maxCount(1), RelicBundleContentsComponent.empty).rarity(Rarity.RARE));

		aerostone = registerRelic("aerostone", Item::new, new Item.Settings());
		amber = registerRelic("amber", Item::new, new Item.Settings());
		colosine = registerRelic("colosine", Item::new, new Item.Settings());
		jasper = registerRelic("jasper", Item::new, new Item.Settings());
		quarite = registerRelic("quarite", Item::new, new Item.Settings());
		ruby = registerRelic("ruby", Item::new, new Item.Settings());

		aerostoneTorch = register(BlockRegistry.aerostoneTorch, (block, settings) -> new VerticallyAttachableBlockItem(block, BlockRegistry.aerostoneWallTorch, Direction.DOWN, settings));
		aerostoneLantern = register(BlockRegistry.aerostoneLantern);

		jasperHelmet = register("jasper_helmet", Item::new, new Item.Settings().armor(MaterialRegistry.Armor.jasper, EquipmentType.HELMET));
		jasperChestplate = register("jasper_chestplate", Item::new, new Item.Settings().armor(MaterialRegistry.Armor.jasper, EquipmentType.CHESTPLATE));
		jasperLeggings = register("jasper_leggings", Item::new, new Item.Settings().armor(MaterialRegistry.Armor.jasper, EquipmentType.LEGGINGS));
		jasperBoots = register("jasper_boots", Item::new, new Item.Settings().armor(MaterialRegistry.Armor.jasper, EquipmentType.BOOTS));

		quaritePickaxe = register("quarite_pickaxe", Item::new, new Item.Settings().pickaxe(MaterialRegistry.Tool.quarite, 1.0F, -2.8F));

		rubySword = register("ruby_sword", Item::new, new Item.Settings().sword(MaterialRegistry.Tool.ruby, 3.0F, -2.4F));

		relicDust = registerRelic("relic_dust", Item::new, new Item.Settings());

		terrorlandsCompass = register("terrorlands_compass", TerrorlandsCompassItem::new, new Item.Settings().maxCount(1));

		redEgg = register(BlockRegistry.redEgg);
		largeRedEgg = register(BlockRegistry.largeRedEgg);
		grayEgg = register(BlockRegistry.grayEgg);
		largeGrayEgg = register(BlockRegistry.largeGrayEgg);

		junglefowl = register("junglefowl", Item::new, new Item.Settings().food(FoodComponents.CHICKEN, ConsumableComponents.RAW_CHICKEN));
		cookedJunglefowl = register("cooked_junglefowl", Item::new, new Item.Settings().food(FoodComponents.COOKED_CHICKEN));

		junglefowlSpawnEgg = register("junglefowl_spawn_egg", (settings) -> new SpawnEggItem(EntityRegistry.junglefowl, settings), new Item.Settings());
		boarSpawnEgg = register("boar_spawn_egg", (settings) -> new SpawnEggItem(EntityRegistry.boar, settings), new Item.Settings());
		fleeciferSpawnEgg = register("fleecifer_spawn_egg", (settings) -> new SpawnEggItem(EntityRegistry.fleecifer, settings), new Item.Settings());
		fleeciferBossSpawnEgg = register("fleecifer_boss_spawn_egg", (settings) -> new SpawnEggItem(EntityRegistry.fleeciferBoss, settings), new Item.Settings(), AutoItemGroup.none);

		fleeciferWool = register("fleecifer_wool", Item::new, new Item.Settings());
		eyeOfFleecifer = register("fleecifer_eye", FleeciferEyeItem::new, new Item.Settings());
		relicBundleUpgradeSmithingTemplate = register("relic_bundle_upgrade_smithing_template", ItemRegistry::createRelicBundleUpgrade, new Item.Settings());
	}

	public static SmithingTemplateItem createRelicBundleUpgrade(Item.Settings settings) {
		return new SmithingTemplateItem(
			Text.translatable(Util.createTranslationKey("item", Data.idOf("smithing_template.relic_bundle_upgrade.applies_to"))).formatted(Formatting.BLUE),
			Text.translatable(Util.createTranslationKey("item", Data.idOf("smithing_template.relic_bundle_upgrade.ingredients"))).formatted(Formatting.BLUE),
			Text.translatable(Util.createTranslationKey("item", Data.idOf("smithing_template.relic_bundle_upgrade.base_slot_description"))),
			Text.translatable(Util.createTranslationKey("item", Data.idOf("smithing_template.relic_bundle_upgrade.additions_slot_description"))),
			List.of(Data.idOf("container/slot/relic_bundle")),
			List.of(Identifier.ofVanilla("container/slot/redstone_dust")),
			settings
		);
	}

	public static Item register(Block block) {
		return register(block, BlockItem::new, AutoItemGroup.survival);
	}

	public static Item register(Block block, AutoItemGroup itemGroup) {
		return register(block, BlockItem::new, itemGroup);
	}

	public static Item register(Block block, BiFunction<Block, Item.Settings, Item> factory) {
		return register(block, factory, new Item.Settings(), AutoItemGroup.survival);
	}

	public static Item register(Block block, BiFunction<Block, Item.Settings, Item> factory, AutoItemGroup itemGroup) {
		return register(block, factory, new Item.Settings(), itemGroup);
	}

	public static Item register(Block block, BiFunction<Block, Item.Settings, Item> factory, Item.Settings settings) {
		return register(block, factory, settings, AutoItemGroup.survival);
	}

	@SuppressWarnings("deprecation")
	public static Item register(Block block, BiFunction<Block, Item.Settings, Item> factory, Item.Settings settings, AutoItemGroup itemGroup) {
		return register(keyOf(block.getRegistryEntry().registryKey()), (itemSettings) -> factory.apply(block, itemSettings), settings.useBlockPrefixedTranslationKey(), itemGroup);
	}

	public static Item register(Identifier id, Function<Item.Settings, Item> factory, Item.Settings settings) {
		return register(id, factory, settings, AutoItemGroup.survival);
	}

	public static Item register(Identifier id, Function<Item.Settings, Item> factory, Item.Settings settings, AutoItemGroup itemGroup) {
		return register(keyOf(id), factory, settings, itemGroup);
	}

	public static Item register(String id, Function<Item.Settings, Item> factory, Item.Settings settings) {
		return register(id, factory, settings, AutoItemGroup.survival);
	}

	public static Item register(String id, Function<Item.Settings, Item> factory, Item.Settings settings, AutoItemGroup itemGroup) {
		return register(Data.idOf(id), factory, settings, itemGroup);
	}

	public static Item registerRelic(Identifier id, Function<Item.Settings, Item> factory, Item.Settings settings, Identifier relicId, AutoItemGroup itemGroup) {
		return register(id, factory, relic(settings, relicId), itemGroup);
	}

	public static Item registerRelic(String id, Function<Item.Settings, Item> factory, Item.Settings settings) {
		return registerRelic(id, factory, settings, AutoItemGroup.survival);
	}

	public static Item registerRelic(String id, Function<Item.Settings, Item> factory, Item.Settings settings, AutoItemGroup itemGroup) {
		Identifier identifier = Data.idOf(id);
		return registerRelic(identifier, factory, settings, identifier, itemGroup);
	}

	public static Item register(RegistryKey<Item> key, Function<Item.Settings, Item> factory, Item.Settings settings) {
		return register(key, factory, settings, AutoItemGroup.survival);
	}

	public static void bootstrap() {
		MaterialRegistry.bootstrap();
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

	public static Item register(RegistryKey<Item> key, Function<Item.Settings, Item> factory, Item.Settings settings, AutoItemGroup itemGroup) {
		Item item = factory.apply(settings.registryKey(key));
		if (item instanceof BlockItem blockItem) blockItem.appendBlocks(Item.BLOCK_ITEMS, item);
		if (itemGroup.equals(AutoItemGroup.survival)) items.add(item);
		else if (itemGroup.equals(AutoItemGroup.creative)) creativeItems.add(item);
		return Registry.register(Registries.ITEM, key, item);
	}

	public enum AutoItemGroup {
		survival,
		creative,
		none;
	}
}
