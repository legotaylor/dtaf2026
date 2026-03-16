/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.gui;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import dev.dannytaylor.dtaf2026.client.config.Config;
import dev.dannytaylor.dtaf2026.client.data.ClientData;
import dev.dannytaylor.dtaf2026.client.gui.screen.LogoConfirmScreen;
import dev.dannytaylor.dtaf2026.common.data.Data;
import dev.dannytaylor.dtaf2026.common.registry.BlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.texture.Sprite;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.profiler.Profilers;

import java.util.List;
import java.util.function.Function;

public class ScreenHelper {
	private static float prevSleepAlpha;
	private static float sleepAlpha;

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
		return ClientData.getMinecraft().getBlockRenderManager().getModels().getModelParticleSprite(getSomniumRealePortalBlockState());
	}

	public static BlockState getSomniumRealePortalBlockState() {
		return BlockRegistry.terrorlandsPortal.getDefaultState();
	}

	public static void renderSleepPortalOverlay(DrawContext context, RenderTickCounter tickCounter) {
		ScreenHelper.setSleepAlpha(MathHelper.lerp(tickCounter.getTickProgress(true), ScreenHelper.getPrevSleepAlpha(), ScreenHelper.getSleepAlpha()));
		renderSleepPortalOverlay(context, ScreenHelper.getSleepAlpha());
	}

	public static void renderSleepPortalOverlay(DrawContext context, RenderPipeline pipeline, float alpha) {
		if (alpha > 0) {
			Profilers.get().push(Data.idOf("sleep").toUnderscoreSeparatedString());
			context.createNewRootLayer();
			context.drawSpriteStretched(pipeline, ScreenHelper.getSomniumRealePortalSprite(), 0, 0, context.getScaledWindowWidth(), context.getScaledWindowHeight(), ((int) (Math.max(0, Math.min(alpha / 100.0F, 1.0F)) * 255.0F) << 24) | 0xFFFFFF);
			Profilers.get().pop();
		}
	}

	public static void renderSleepPortalOverlay(DrawContext context, float alpha) {
		renderSleepPortalOverlay(context, RenderPipelines.GUI_TEXTURED, alpha);
	}
}
