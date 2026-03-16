/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.item;

import dev.dannytaylor.dtaf2026.common.registry.tagkey.TagRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistryEvents;

public class FuelRegistry {
	public static void register(FuelRegistryEvents.BuildCallback buildCallback) {
		FuelRegistryEvents.BUILD.register(buildCallback);
	}

	public static void bootstrap() {
		register(FuelRegistry::build);
	}

	private static void build(net.minecraft.item.FuelRegistry.Builder builder, FuelRegistryEvents.Context context) {
		builder.add(TagRegistry.Item.bark, context.baseSmeltTime());
	}
}
