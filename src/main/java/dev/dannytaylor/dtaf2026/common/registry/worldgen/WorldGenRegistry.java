/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.worldgen;

public class WorldGenRegistry {
	public static void bootstrap() {
		DimensionRegistry.bootstrap();
		DecoratorRegistry.bootstrap();
		SaplingGeneratorRegistry.bootstrap();
	}
}
