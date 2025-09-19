/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry;

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
	protected static final RegistryKey<ItemGroup> dtaf2026 = register(Data.idOf(Data.getModId()), FabricItemGroup.builder().icon(() -> new ItemStack(ItemRegistry.maple.bark)).displayName(Text.translatable("itemGroup." + Data.getModId(), Text.translatable(Data.getModId() + ".name"))).build());
	protected static final RegistryKey<ItemGroup> dtaf2026Creative = register(Data.idOf(Data.getModId() + "_creative"), FabricItemGroup.builder().icon(() -> {
		ItemStack stack = new ItemStack(ItemRegistry.maple.bark);
		stack.apply(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true, (b) -> b);
		return stack;
	}).displayName(Text.translatable("itemGroup." + Data.getModId() + "Creative", Text.translatable(Data.getModId() + ".name"))).build());
	
	public static void bootstrap() {
		ItemGroupEvents.modifyEntriesEvent(dtaf2026).register((content) -> {
			for (SupportedWoodItemSet itemSet : ItemRegistry.woodItemSets) itemSet.addItemGroupEntries(content, false);
		});
		ItemGroupEvents.modifyEntriesEvent(dtaf2026Creative).register((content) -> {
			for (SupportedWoodItemSet itemSet : ItemRegistry.woodItemSets) itemSet.addItemGroupEntries(content, true);
		});
	}
	
	public static RegistryKey<ItemGroup> register(Identifier id, ItemGroup itemGroup) {
		RegistryKey<ItemGroup> key = RegistryKey.of(Registries.ITEM_GROUP.getKey(), id);
		Registry.register(Registries.ITEM_GROUP, key, itemGroup);
		return key;
	}
}
