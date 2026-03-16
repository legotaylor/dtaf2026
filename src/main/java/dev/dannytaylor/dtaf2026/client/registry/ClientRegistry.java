/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry;

import dev.dannytaylor.dtaf2026.client.registry.block.ClientBlockRegistry;
import dev.dannytaylor.dtaf2026.client.registry.dimension.ClientDimensionRegistry;
import dev.dannytaylor.dtaf2026.client.registry.entity.ClientEntityRegistry;
import dev.dannytaylor.dtaf2026.client.registry.item.ClientItemRegistry;
import dev.dannytaylor.dtaf2026.client.registry.keybinds.KeybindRegistry;
import dev.dannytaylor.dtaf2026.client.registry.networking.ClientNetworkingRegistry;
import dev.dannytaylor.dtaf2026.client.registry.particle.ClientParticleRegistry;
import dev.dannytaylor.dtaf2026.client.registry.pipeline.PipelineRegistry;

public class ClientRegistry {
	public static void bootstrap() {
		ClientParticleRegistry.bootstrap();
		ClientEntityRegistry.bootstrap();
		ClientBlockRegistry.bootstrap();
		ClientItemRegistry.bootstrap();
		ClientDimensionRegistry.bootstrap();
		PipelineRegistry.bootstrap();
		ClientNetworkingRegistry.bootstrap();
		KeybindRegistry.bootstrap();
	}
}
