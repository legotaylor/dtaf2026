/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.block;

import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;

public class FlammableRegistry {
	public static void bootstrap() {
		for (SupportedWoodBlockSet woodSet : BlockRegistry.woodBlockSets) registerWoodSet(woodSet);
	}

	public static void registerWoodSet(SupportedWoodBlockSet woodSet) {
		FlammableBlockRegistry registry = FlammableBlockRegistry.getDefaultInstance();
		registry.add(woodSet.log, 5, 5);
		registry.add(woodSet.wood, 5, 5);
		registry.add(woodSet.leaves, 5, 5);
		registry.add(woodSet.planks, 5, 20);
		registry.add(woodSet.sapling, 30, 60);
	}
}
