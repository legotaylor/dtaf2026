/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry;

import net.fabricmc.fabric.api.registry.FuelRegistryEvents;

public class FuelRegistry {
	public static void register(FuelRegistryEvents.BuildCallback buildCallback) {
		FuelRegistryEvents.BUILD.register(buildCallback);
	}

	public static void bootstrap() {
		register((builder, context) -> {
			builder.add(TagRegistry.Item.bark, context.baseSmeltTime());
		});
	}
}
