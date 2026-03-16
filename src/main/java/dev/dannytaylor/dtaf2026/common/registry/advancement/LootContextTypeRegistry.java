/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.advancement;

import dev.dannytaylor.dtaf2026.common.data.Data;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.context.ContextType;

import java.util.function.Consumer;

public class LootContextTypeRegistry {
	public static final ContextType fleeciferFight;

	static {
		fleeciferFight = register("fleecifer_fight", builder -> builder.require(LootContextParameters.THIS_ENTITY).require(LootContextParameterRegistry.fleeciferFightFinished).require(LootContextParameterRegistry.fleeciferBossPhase));
	}

	public static void bootstrap() {
	}

	public static ContextType register(String path, Consumer<ContextType.Builder> type) {
		return register(Data.idOf(path), type);
	}

	public static ContextType register(Identifier identifier, Consumer<ContextType.Builder> type) {
		ContextType.Builder builder = new ContextType.Builder();
		type.accept(builder);
		ContextType contextType = builder.build();
		ContextType contextType2 = LootContextTypes.MAP.put(identifier, contextType);
		if (contextType2 != null) {
			throw new IllegalStateException("Loot table parameter set " + identifier + " is already registered");
		} else {
			return contextType;
		}
	}
}
