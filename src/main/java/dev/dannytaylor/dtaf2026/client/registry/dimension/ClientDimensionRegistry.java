/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry.dimension;

import dev.dannytaylor.dtaf2026.client.data.ClientData;
import dev.dannytaylor.dtaf2026.common.registry.worldgen.DimensionRegistry;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class ClientDimensionRegistry {
	private static final Map<Identifier, DimensionEffects> effectsMap = new HashMap<>();

	public static void bootstrap() {
		addEffectType(DimensionRegistry.somniumReale.id(), new SomniumRealeEffect());
		addEffectType(DimensionRegistry.theTerrorlands.id(), new TheTerrorlandsEffect());
	}

	public static void addEffectType(Identifier id, DimensionEffects effects) {
		effectsMap.putIfAbsent(id, effects);
	}

	public static DimensionEffects getEffectType(Identifier id) {
		return effectsMap.getOrDefault(id, null);
	}

	public static boolean isAbstractSomniumReale(ClientWorld world) {
		return world != null && world.getDimensionEffects() instanceof AbstractSomniumRealeEffect;
	}

	public static boolean isAbstractSomniumReale() {
		return isAbstractSomniumReale(ClientData.getMinecraft().world);
	}

	public static boolean isSomniumReale(ClientWorld world) {
		return world != null && world.getDimensionEffects() instanceof SomniumRealeEffect;
	}

	public static boolean isSomniumReale() {
		return isSomniumReale(ClientData.getMinecraft().world);
	}

	public static boolean isSomniaMetus(ClientWorld world) {
		return world != null && world.getDimensionEffects() instanceof TheTerrorlandsEffect;
	}

	public static boolean isSomniaMetus() {
		return isSomniaMetus(ClientData.getMinecraft().world);
	}
}
