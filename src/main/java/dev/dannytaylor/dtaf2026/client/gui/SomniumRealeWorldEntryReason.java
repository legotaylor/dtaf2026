/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.gui;

import net.minecraft.client.gui.screen.DownloadingTerrainScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SomniumRealeWorldEntryReason {
	private static final List<String> worldEntryReasons = new ArrayList<>();
	private static final Map<String, DownloadingTerrainScreen.WorldEntryReason> registeredWorldEntryReasons = new HashMap<>();

	public static void register(String id) {
		if (!worldEntryReasons.contains(id)) worldEntryReasons.add(id);
		else System.err.println("Entry reason with id '" + id + "' already registered!");
	}

	public static List<String> getWorldEntryReasons() {
		return worldEntryReasons;
	}

	public static void registerCustom(String id, DownloadingTerrainScreen.WorldEntryReason reason) {
		registeredWorldEntryReasons.put(id, reason);
	}

	public static Map<String, DownloadingTerrainScreen.WorldEntryReason> getCustomWorldEntryReasons() {
		return registeredWorldEntryReasons;
	}

	public static void bootstrap() {
		register("SOMNIUM_REALE");
	}
}
