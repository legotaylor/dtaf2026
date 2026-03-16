/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry.keybinds;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;

public class HoldPerspective {
	private static Perspective beforePressed;
	private static boolean wasBackPressed;
	private static boolean wasFrontPressed;

	public static void bootstrap() {
		ClientTickEvents.END_CLIENT_TICK.register(HoldPerspective::tick);
	}

	private static void tick(MinecraftClient client) {
		// Back
		if (!wasBackPressed && KeybindRegistry.holdPerspectiveBack.isPressed()) {
			wasBackPressed = true;
			set(client, wasFrontPressed, false);
		} else if (wasBackPressed && !KeybindRegistry.holdPerspectiveBack.isPressed()) {
			wasBackPressed = false;
			client.options.setPerspective(wasFrontPressed ? Perspective.THIRD_PERSON_FRONT : beforePressed);
		}
		// Front
		if (!wasFrontPressed && KeybindRegistry.holdPerspectiveFront.isPressed()) {
			wasFrontPressed = true;
			set(client, wasBackPressed, true);
		} else if (wasFrontPressed && !KeybindRegistry.holdPerspectiveFront.isPressed()) {
			wasFrontPressed = false;
			client.options.setPerspective(wasBackPressed ? Perspective.THIRD_PERSON_BACK : beforePressed);
		}
		if (!wasFrontPressed && !wasBackPressed && beforePressed != null) beforePressed = null;
	}

	private static void set(MinecraftClient client, boolean isHolding, boolean isFront) {
		if (!isHolding) beforePressed = client.options.getPerspective();
		client.options.setPerspective(isFront ? Perspective.THIRD_PERSON_FRONT : Perspective.THIRD_PERSON_BACK);
	}
}
