/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.advancement;

import dev.dannytaylor.dtaf2026.common.data.Data;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class CriteriaRegistry {
	public static final FleeciferFightCriterion fleeciferFight;

	static {
		fleeciferFight = register("fleecifer_fight", new FleeciferFightCriterion());
	}

	public static void bootstrap() {
	}

	public static <T extends Criterion<?>> T register(Identifier identifier, T criterion) {
		return Registry.register(Registries.CRITERION, identifier, criterion);
	}

	public static <T extends Criterion<?>> T register(String path, T criterion) {
		return register(Data.idOf(path), criterion);
	}
}
