/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry.item;

import dev.dannytaylor.dtaf2026.common.data.Data;
import dev.dannytaylor.dtaf2026.common.registry.ItemRegistry;
import dev.dannytaylor.dtaf2026.common.registry.item.SupportedWoodItemSet;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ItemGroupRegistry {
	protected static final RegistryKey<ItemGroup> dtaf2026 = register(Data.idOf(Data.getModId()), FabricItemGroup.builder().icon(() -> new ItemStack(ItemRegistry.grassBlock)).displayName(Text.translatable("itemGroup." + Data.getModId(), Text.translatable(Data.getModId() + ".name"))).build());
	protected static final RegistryKey<ItemGroup> dtaf2026Creative = register(Data.idOf(Data.getModId() + "_creative"), FabricItemGroup.builder().icon(() -> {
		ItemStack stack = ItemRegistry.maple.creativeBlocks.log.zero.getDefaultStack();
		stack.apply(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true, (b) -> b);
		return stack;
	}).displayName(Text.translatable("itemGroup." + Data.getModId() + "Creative", Text.translatable(Data.getModId() + ".name"))).build());
	
	public static void bootstrap() {
		ItemGroupEvents.modifyEntriesEvent(dtaf2026).register((content) -> {
			// Dirts
			content.add(ItemRegistry.grassBlock);
			content.add(ItemRegistry.dryGrassBlock);
			content.add(ItemRegistry.dirtBlock);
			// TODO: Stone Block/Item Sets, should specify if adds ores; probably going to be in v1.1.
			// Stone
			content.add(ItemRegistry.stoneBlock);
			content.add(ItemRegistry.stoneStairsBlock);
			content.add(ItemRegistry.stoneSlabBlock);
			content.add(ItemRegistry.jasperOreBlock);
			// Cobblestone
			content.add(ItemRegistry.cobblestoneBlock);
			content.add(ItemRegistry.cobblestoneStairsBlock);
			content.add(ItemRegistry.cobblestoneSlabBlock);
			content.add(ItemRegistry.cobblestoneWallBlock);
			// Basalt
			content.add(ItemRegistry.deepslateBlock);
			content.add(ItemRegistry.deepslateStairsBlock);
			content.add(ItemRegistry.deepslateSlabBlock);
			content.add(ItemRegistry.deepslateJasperOreBlock);
			// Cobbled Basalt
			content.add(ItemRegistry.cobbledDeepslateBlock);
			content.add(ItemRegistry.cobbledDeepslateStairsBlock);
			content.add(ItemRegistry.cobbledDeepslateSlabBlock);
			content.add(ItemRegistry.cobbledDeepslateWallBlock);
			// Wood Sets
			for (SupportedWoodItemSet itemSet : ItemRegistry.woodItemSets) itemSet.addItemGroupEntries(content, false);
			// Creaking Hearts
			content.add(ItemRegistry.mapleCreakingHeart);
			content.add(ItemRegistry.ceruleanCreakingHeart);
			// Relics
			content.add(ItemRegistry.jasper.getDefaultStack());
			// Relic Bundles
			content.add(ItemRegistry.nightRelicBundle.getDefaultStack());
			content.add(ItemRegistry.dayRelicBundle.getDefaultStack());
			content.add(ItemRegistry.relicBundle.getDefaultStack());
			// Entity Drops
			content.add(ItemRegistry.redEgg.getDefaultStack());
			content.add(ItemRegistry.largeRedEgg.getDefaultStack());
			content.add(ItemRegistry.grayEgg.getDefaultStack());
			content.add(ItemRegistry.largeGrayEgg.getDefaultStack());
			content.add(ItemRegistry.junglefowl.getDefaultStack());
			content.add(ItemRegistry.cookedJunglefowl.getDefaultStack());
			content.add(ItemRegistry.fleeciferWool.getDefaultStack());
			content.add(ItemRegistry.eyeOfFleecifer.getDefaultStack());
			// Spawn Eggs
			content.add(ItemRegistry.junglefowlSpawnEgg.getDefaultStack());
			content.add(ItemRegistry.boarSpawnEgg.getDefaultStack());
			content.add(ItemRegistry.fleeciferSpawnEgg.getDefaultStack());
		});
		ItemGroupEvents.modifyEntriesEvent(dtaf2026Creative).register((content) -> {
			// Creative Wood Sets
			for (SupportedWoodItemSet itemSet : ItemRegistry.woodItemSets) itemSet.addItemGroupEntries(content, true);
		});
	}
	
	public static RegistryKey<ItemGroup> register(Identifier id, ItemGroup itemGroup) {
		RegistryKey<ItemGroup> key = RegistryKey.of(Registries.ITEM_GROUP.getKey(), id);
		Registry.register(Registries.ITEM_GROUP, key, itemGroup);
		return key;
	}
}
