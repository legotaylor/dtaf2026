/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry;

public class ClientRegistry {
	public static void bootstrap() {
		ClientParticleRegistry.bootstrap();
		ClientEntityRegistry.bootstrap();
		ClientBlockRegistry.bootstrap();
		ClientItemRegistry.bootstrap();
		ClientDimensionRegistry.bootstrap();
		PipelineRegistry.bootstrap();
		ClientNetworkingRegistry.bootstrap();
	}
}
