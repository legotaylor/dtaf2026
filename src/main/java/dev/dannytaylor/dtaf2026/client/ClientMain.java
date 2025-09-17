/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client;

import dev.dannytaylor.dtaf2026.client.config.Config;
import dev.dannytaylor.dtaf2026.client.gui.ScreenHelper;
import dev.dannytaylor.dtaf2026.client.registry.ClientRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

public class ClientMain implements ClientModInitializer {
	public void onInitializeClient() {
		Config.bootstrap();
		ClientRegistry.bootstrap();
		ClientTickEvents.START_CLIENT_TICK.register(ClientMain::tick);
	}
	public static void tick(MinecraftClient client) {
		ScreenHelper.tick();
	}
}
