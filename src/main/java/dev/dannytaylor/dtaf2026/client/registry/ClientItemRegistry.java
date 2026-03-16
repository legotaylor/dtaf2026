/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry;

import dev.dannytaylor.dtaf2026.client.registry.item.ItemGroupRegistry;
import dev.dannytaylor.dtaf2026.client.registry.item.ItemModelRegistry;
import dev.dannytaylor.dtaf2026.client.registry.item.TooltipRegistry;

public class ClientItemRegistry {
	public static void bootstrap() {
		ItemModelRegistry.bootstrap();
		TooltipRegistry.bootstrap();
		ItemGroupRegistry.bootstrap();
	}
}
