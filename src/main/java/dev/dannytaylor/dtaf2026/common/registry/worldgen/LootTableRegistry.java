/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.worldgen;

import dev.dannytaylor.dtaf2026.common.data.Data;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class LootTableRegistry {
	public static final RegistryKey<LootTable> junglefowlLayGameplay;

	static {
		junglefowlLayGameplay = register("gameplay/junglefowl_lay");
	}

	public static void bootstrap() {
	}

	public static RegistryKey<LootTable> register(String id) {
		return register(Data.idOf(id));
	}

	public static RegistryKey<LootTable> register(Identifier id) {
		return RegistryKey.of(RegistryKeys.LOOT_TABLE, id);
	}
}
