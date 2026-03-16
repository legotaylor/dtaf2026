/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry.keybinds;

import dev.dannytaylor.dtaf2026.common.data.Data;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeybindRegistry {
	public static final KeyBinding holdPerspectiveBack;
	public static final KeyBinding holdPerspectiveFront;
	public static final KeyBinding zoom;

	static {
		holdPerspectiveBack = register(Data.getModId(), Data.getModId(), "hold_perspective_back", GLFW.GLFW_KEY_Z);
		holdPerspectiveFront = register(Data.getModId(), Data.getModId(), "hold_perspective_front", GLFW.GLFW_KEY_R);
		zoom = register(Data.getModId(), Data.getModId(), "zoom", GLFW.GLFW_KEY_V);
	}

	public static void bootstrap() {
		ClientTickEvents.END_CLIENT_TICK.register(KeybindRegistry::tick);
	}

	public static void tick(MinecraftClient client) {
		HoldPerspective.tick(client);
		Zoom.tick(client);
	}

	public static KeyBinding register(String namespace, String category, String key, int keyCode) {
		return KeyBindingHelper.registerKeyBinding(new KeyBinding("gui." + namespace + ".keybinding." + key, InputUtil.Type.KEYSYM, keyCode, "gui." + namespace + ".keybindings.category." + category));
	}
}
