/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry.entity;

public class ClientEntityRegistry {
	public static void bootstrap() {
		ClientVariantRegistries.bootstrap();
		EntityModelRegistry.bootstrap();
		EntityRendererRegistry.bootstrap();
	}
}
