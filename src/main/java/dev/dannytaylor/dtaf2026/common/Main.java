/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common;

import dev.dannytaylor.dtaf2026.common.registry.Registry;
import net.fabricmc.api.ModInitializer;

public class Main implements ModInitializer {
	public void onInitialize() {
		Registry.bootstrap();
	}
}
