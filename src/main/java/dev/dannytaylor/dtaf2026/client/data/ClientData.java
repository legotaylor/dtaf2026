/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.data;

import net.minecraft.client.MinecraftClient;

public class ClientData {
	public static MinecraftClient getMinecraft() {
		return MinecraftClient.getInstance();
	}
}
