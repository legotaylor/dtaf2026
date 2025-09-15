/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.gui;

import dev.dannytaylor.dtaf2026.client.data.ClientData;
import dev.dannytaylor.dtaf2026.client.gui.screen.LogoConfirmScreen;
import dev.dannytaylor.dtaf2026.common.data.Data;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class TitleScreenHelper {
	public static boolean hasSeenFlashingLightsWarning;
	public static int getTitleYOffset() {
		return -18;
	}
	public static void showFlashingLightsWarning() {
		if (!TitleScreenHelper.hasSeenFlashingLightsWarning) {
			//if (AprilFoolsConfig.config.showPhotosensitivityWarningOnStartup.value()) {
				Screen currentScreen = ClientData.getMinecraft().currentScreen;
				ClientData.getMinecraft().setScreen(new LogoConfirmScreen((confirm) -> {
					if (confirm) {
						TitleScreenHelper.hasSeenFlashingLightsWarning = true;
						ClientData.getMinecraft().setScreen(currentScreen);
					} else ClientData.getMinecraft().scheduleStop();
				}, Text.translatable(Data.getModId() + ".flashing_lights").formatted(Formatting.BOLD), Text.translatable(Data.getModId() + ".flashing_lights.description"), ScreenTexts.PROCEED, Text.translatable("menu.quit")));
			//} else TitleScreenHelper.hasSeenFlashingLightsWarning = true;
		}
	}
}
