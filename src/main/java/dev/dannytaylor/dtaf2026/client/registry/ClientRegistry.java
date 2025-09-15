/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry;

public class ClientRegistry {
	public static void bootstrap() {
		ItemGroupRegistry.bootstrap();
		ClientDimensionRegistry.bootstrap();
		PipelineRegistry.bootstrap();
	}
}
