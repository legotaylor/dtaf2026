package dev.dannytaylor.dtaf2026.common.registry.loot;

import dev.dannytaylor.dtaf2026.common.data.Data;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class LootTableRegistry {
	public static final RegistryKey<LootTable> abandoned_mineshaft_chest;

	static {
		abandoned_mineshaft_chest = register("chests/abandoned_mineshaft");
	}

	public static RegistryKey<LootTable> register(String path) {
		return register(Data.idOf(path));
	}

	public static RegistryKey<LootTable> register(Identifier identifier) {
		return LootTables.registerLootTable(RegistryKey.of(RegistryKeys.LOOT_TABLE, identifier));
	}
}
