/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.advancement;

import dev.dannytaylor.dtaf2026.common.data.Data;
import net.minecraft.util.Identifier;
import net.minecraft.util.context.ContextParameter;

public class LootContextParameterRegistry {
	public static final ContextParameter<Boolean> fleeciferFightFinished;
	public static final ContextParameter<Integer> fleeciferBossPhase;

	static {
		fleeciferFightFinished = of("fleecifer_fight_finished");
		fleeciferBossPhase = of("fleecifer_boss_phase");
	}

	public static void bootstrap() {
	}

	public static <T> ContextParameter<T> of(Identifier identifier) {
		return new ContextParameter<>(identifier);
	}

	public static <T> ContextParameter<T> of(String path) {
		return of(Data.idOf(path));
	}
}
