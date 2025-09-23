/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.data;

import dev.dannytaylor.dtaf2026.common.data.Data;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class ClientData {
	public static MinecraftClient getMinecraft() {
		return MinecraftClient.getInstance();
	}

	public static MutableText getText(String key, Object... args) {
		return getText(Data.getModId(), key, args);
	}

	public static MutableText getText(String namespace, String key, Object... args) {
		return getTranslatableText(namespace + "." + key, args);
	}

	private static MutableText getTranslatableText(String key, Object... args) {
		return Text.translatable(key, args);
	}
}
