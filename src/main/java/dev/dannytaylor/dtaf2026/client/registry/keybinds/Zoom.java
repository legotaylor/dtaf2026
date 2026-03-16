/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry.keybinds;

import dev.dannytaylor.dtaf2026.client.config.Config;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;

public class Zoom {
	public static float zoomLevel = Config.instance.zoomLevel.value();
	public static float prevMultiplier = 1.0F;
	public static float multiplier = 1.0F;
	public static float fov = 70.0F;
	public static float zoomFov = 70.0F;
	public static boolean hasUpdated;

	public static void tick(MinecraftClient client) {
		if (!isActive() && hasUpdated) {
			hasUpdated = false;
			Config.instance.zoomLevel.setValue(zoomLevel, true);
		}
	}

	public static void updateMultiplier() {
		prevMultiplier = multiplier;
		float updatedMultiplier;
		if (!isActive()) updatedMultiplier = 1.0F;
		else updatedMultiplier = (float) (1.0F - (Math.log(getZoomLevel() + 1.0F) / Math.log(100.0 + 1.0F)));
		multiplier = MathHelper.lerp(1.0F, prevMultiplier, (prevMultiplier + updatedMultiplier) * 0.5F);
	}

	public static boolean isActive() {
		return KeybindRegistry.zoom.isPressed();
	}

	public static float getZoomLevel() {
		return Math.clamp(zoomLevel, 0.0F, 100.0F);
	}

	public static void setZoomLevel(float value) {
		zoomLevel = Math.clamp(value, 0.0F, 100.0F);
		hasUpdated = true;
	}

	public static double getMouseSensitivity(MinecraftClient client) {
		return Math.pow(client.options.getMouseSensitivity().getValue() * 0.6000000238418579F + 0.20000000298023224F, 3.0F) * 8.0F;
	}

	public static void adjustZoomLevel(int amount, float multiplier) {
		setZoomLevel(zoomLevel + (amount * multiplier));
	}

	public static void resetZoomLevel() {
		setZoomLevel(Config.instance.zoomLevel.getDefaultValue());
	}

	public static float getLimitFOV(float input) {
		return MathHelper.clamp(input, 0.1F, 179.9F);
	}

	public static double getMultiplierFromFOV() {
		return zoomFov / fov;
	}
}
