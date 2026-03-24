/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry.item;

import dev.dannytaylor.dtaf2026.common.data.Data;
import dev.dannytaylor.dtaf2026.common.registry.item.ItemRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
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
			for (Item item : ItemRegistry.items) content.add(item);
		});
		ItemGroupEvents.modifyEntriesEvent(dtaf2026Creative).register((content) -> {
			for (Item item : ItemRegistry.creativeItems) content.add(item);
		});
	}
	
	public static RegistryKey<ItemGroup> register(Identifier id, ItemGroup itemGroup) {
		RegistryKey<ItemGroup> key = RegistryKey.of(Registries.ITEM_GROUP.getKey(), id);
		Registry.register(Registries.ITEM_GROUP, key, itemGroup);
		return key;
	}
}
