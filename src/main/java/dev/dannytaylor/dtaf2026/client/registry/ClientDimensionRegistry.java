/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry;

import dev.dannytaylor.dtaf2026.client.registry.dimension.SomniumRealeEffect;
import dev.dannytaylor.dtaf2026.common.registry.DimensionRegistry;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class ClientDimensionRegistry {
	private static final Map<Identifier, DimensionEffects> effectsMap = new HashMap<>();

	public static void bootstrap() {
		addEffectType(DimensionRegistry.somniumReale.id(), new SomniumRealeEffect());
	}

	public static void addEffectType(Identifier id, DimensionEffects effects) {
		effectsMap.putIfAbsent(id, effects);
	}

	public static DimensionEffects getEffectType(Identifier id) {
		return effectsMap.getOrDefault(id, null);
	}
}
