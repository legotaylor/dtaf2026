/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common;

import dev.dannytaylor.dtaf2026.common.registry.Registry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.world.ServerWorld;

public class Main implements ModInitializer {
	public void onInitialize() {
		Registry.bootstrap();
		ServerTickEvents.END_WORLD_TICK.register(this::onWorldTick);
	}

	public void onWorldTick(ServerWorld serverWorld) {
		Registry.worldTick(serverWorld);
	}
}
