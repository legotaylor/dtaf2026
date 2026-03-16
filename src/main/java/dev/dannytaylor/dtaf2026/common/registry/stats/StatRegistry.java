/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.stats;

import dev.dannytaylor.dtaf2026.common.data.Data;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.StatType;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class StatRegistry {
	public static final Identifier fleeciferBossPhases;
	public static final Identifier fleeciferBossWon;

	static {
		fleeciferBossPhases = register("fleecifer_boss_phases", StatFormatter.DEFAULT);
		fleeciferBossWon = register("fleecifer_boss_won", StatFormatter.DEFAULT);
	}

	public static void bootstrap() {
	}

	public static Identifier register(String path, StatFormatter formatter) {
		return register(Data.idOf(path), formatter);
	}

	public static Identifier register(Identifier identifier, StatFormatter formatter) {
		Registry.register(Registries.CUSTOM_STAT, identifier, identifier);
		Stats.CUSTOM.getOrCreateStat(identifier, formatter);
		return identifier;
	}

	public static <T> StatType<T> registerType(String path, Registry<T> registry) {
		return registerType(Data.idOf(path), registry);
	}

	public static <T> StatType<T> registerType(Identifier identifier, Registry<T> registry) {
		return Registry.register(Registries.STAT_TYPE, identifier, new StatType<>(registry, Text.translatable("stat_type." + identifier.getNamespace() + "." + identifier.getPath())));
	}
}
