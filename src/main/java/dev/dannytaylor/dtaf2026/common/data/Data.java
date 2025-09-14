/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.data;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

public class Data {
	private static final String modId;

	public static Identifier idOf(String path) {
		return Identifier.of(getModId(), path);
	}

	public static String getModId() {
		return modId;
	}

	public static boolean isModInstalled(String modId) {
		return FabricLoader.getInstance().isModLoaded(modId);
	}

	static {
		modId = "dtaf2026";
	}
}
