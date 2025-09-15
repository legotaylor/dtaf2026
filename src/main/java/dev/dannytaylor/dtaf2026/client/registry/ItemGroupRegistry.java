/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry;

import dev.dannytaylor.dtaf2026.common.data.Data;
import dev.dannytaylor.dtaf2026.common.registry.ItemRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ItemGroupRegistry {
	protected static final RegistryKey<ItemGroup> dtaf2026 = register(Data.idOf(Data.getModId()), FabricItemGroup.builder().icon(() -> new ItemStack(ItemRegistry.maple_bark)).displayName(Text.translatable("itemGroup." + Data.getModId(), Text.translatable(Data.getModId() + ".name"))).build());
	
	public static void bootstrap() {
		ItemGroupEvents.modifyEntriesEvent(dtaf2026).register((content) -> {
			content.add(ItemRegistry.maple_log);
			content.add(ItemRegistry.stripped_maple_log);
			content.add(ItemRegistry.twice_stripped_maple_log);
			content.add(ItemRegistry.maple_wood);
			content.add(ItemRegistry.stripped_maple_wood);
			content.add(ItemRegistry.twice_stripped_maple_wood);
			content.add(ItemRegistry.maple_bark);
		});
	}
	
	public static RegistryKey<ItemGroup> register(Identifier id, ItemGroup itemGroup) {
		RegistryKey<ItemGroup> key = RegistryKey.of(Registries.ITEM_GROUP.getKey(), id);
		Registry.register(Registries.ITEM_GROUP, key, itemGroup);
		return key;
	}
}
