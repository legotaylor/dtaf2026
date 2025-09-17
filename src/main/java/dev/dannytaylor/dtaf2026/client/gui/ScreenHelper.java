/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.gui;

import dev.dannytaylor.dtaf2026.client.config.Config;
import dev.dannytaylor.dtaf2026.client.data.ClientData;
import dev.dannytaylor.dtaf2026.client.gui.screen.LogoConfirmScreen;
import dev.dannytaylor.dtaf2026.common.data.Data;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.texture.Sprite;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.function.Function;

public class ScreenHelper {
	private static float prevSleepAlpha;
	private static float sleepAlpha;
	private static Sprite somniumRealePortalSprite;

	public static int getTitleYOffset() {
		return -18;
	}

	public static boolean showFlashingLightsWarning(List<Function<Runnable, Screen>> list) {
		if (!Config.instance.skipFlashingLightsWarning.value()) {
			list.add((onClose) -> new LogoConfirmScreen((confirm) -> {
				if (confirm) onClose.run();
				else ClientData.getMinecraft().scheduleStop();
			},
				() -> Config.instance.skipFlashingLightsWarning.setValue(true, true),
				Text.translatable(Data.getModId() + ".flashingLights").formatted(Formatting.BOLD),
				Text.translatable(Data.getModId() + ".flashingLights.description", Text.translatable(Data.getModId() + ".name")),
				ScreenTexts.PROCEED,
				Text.translatable("menu.quit"),
				Text.translatable(Data.getModId() + ".flashingLights.check")
			));
			return true;
		}
		return false;
	}

	public static void tick() {
		tickSleep(ClientData.getMinecraft().player);
	}

	private static void tickSleep(PlayerEntity player) {
		if (player != null) {
			if (player.isSleeping() && sleepAlpha < 100) setSleepAlpha(getSleepAlpha() + 1);
			else if (!player.isSleeping() && sleepAlpha > 0) setSleepAlpha(0);
		}
	}

	public static void setSleepAlpha(float value) {
		prevSleepAlpha = sleepAlpha;
		sleepAlpha = Math.clamp(value, 0, 100);
	}

	public static float getPrevSleepAlpha() {
		return Math.clamp(prevSleepAlpha, 0, 100);
	}

	public static float getSleepAlpha() {
		return Math.clamp(sleepAlpha, 0, 100);
	}

	public static Sprite getSomniumRealePortalSprite() {
		if (somniumRealePortalSprite == null) somniumRealePortalSprite = ClientData.getMinecraft().getBlockRenderManager().getModels().getModelParticleSprite(getSomniumRealePortalBlockState());
		return somniumRealePortalSprite;
	}

	public static BlockState getSomniumRealePortalBlockState() {
		return Blocks.NETHER_PORTAL.getDefaultState();
	}
}
