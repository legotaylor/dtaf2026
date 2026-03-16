/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.advancement;

import com.mojang.serialization.MapCodec;
import dev.dannytaylor.dtaf2026.common.data.Data;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class LootConditionTypeRegistry {
	public static final LootConditionType fleeciferFightFinished;
	public static final LootConditionType fleeciferBossPhase;

	static {
		fleeciferFightFinished = register("fleecifer_fight_finished", FleeciferFightFinishedCondition.CODEC);
		fleeciferBossPhase = register("fleecifer_boss_phase", FleeciferBossPhaseCondition.CODEC);
	}

	public static void bootstrap() {
	}

	public static LootConditionType register(Identifier identifier, MapCodec<? extends LootCondition> codec) {
		return Registry.register(Registries.LOOT_CONDITION_TYPE, identifier, new LootConditionType(codec));
	}

	public static LootConditionType register(String path, MapCodec<? extends LootCondition> codec) {
		return register(Data.idOf(path), codec);
	}
}
