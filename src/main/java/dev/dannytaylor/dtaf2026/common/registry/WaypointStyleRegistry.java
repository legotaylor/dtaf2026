/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry;

import net.minecraft.registry.RegistryKey;
import net.minecraft.world.waypoint.WaypointStyle;
import net.minecraft.world.waypoint.WaypointStyles;

public class WaypointStyleRegistry {
	public static final RegistryKey<WaypointStyle> fleeciferBoss;

	static {
		fleeciferBoss = WaypointStyles.of("fleecifer_boss");
	}

	public static void bootstrap() {
	}
}
