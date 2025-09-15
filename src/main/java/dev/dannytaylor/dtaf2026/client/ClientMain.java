/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client;

import dev.dannytaylor.dtaf2026.client.registry.ClientDimensionRegistry;
import dev.dannytaylor.dtaf2026.client.registry.ClientRegistry;
import dev.dannytaylor.dtaf2026.client.registry.ItemGroupRegistry;
import net.fabricmc.api.ClientModInitializer;

public class ClientMain implements ClientModInitializer {
	public void onInitializeClient() {
		ClientRegistry.bootstrap();
	}
}
