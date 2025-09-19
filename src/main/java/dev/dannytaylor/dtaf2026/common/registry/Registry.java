/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry;

public class Registry {
	public static void bootstrap() {
		BlockRegistry.bootstrap();
		ItemRegistry.bootstrap();
		FuelRegistry.bootstrap();
		DimensionRegistry.bootstrap();
		AttributeModifierRegistry.bootstrap();
	}
}
