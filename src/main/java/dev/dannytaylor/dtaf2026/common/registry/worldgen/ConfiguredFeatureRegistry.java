/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.worldgen;

import dev.dannytaylor.dtaf2026.common.data.Data;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.ConfiguredFeature;

public class ConfiguredFeatureRegistry {
	public static final RegistryKey<ConfiguredFeature<?, ?>> mapleTree = registerKey("maple_tree");
	public static final RegistryKey<ConfiguredFeature<?, ?>> ceruleanTree = registerKey("cerulean_tree");

	public static RegistryKey<ConfiguredFeature<?, ?>> registerKey(String path) {
		return registerKey(Data.idOf(path));
	}

	public static RegistryKey<ConfiguredFeature<?, ?>> registerKey(Identifier id) {
		return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, id);
	}
}
