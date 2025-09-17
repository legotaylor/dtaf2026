/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.mixin.gui;

import dev.dannytaylor.dtaf2026.client.data.ClientData;
import dev.dannytaylor.dtaf2026.client.gui.ScreenHelper;
import dev.dannytaylor.dtaf2026.common.data.Data;
import dev.dannytaylor.dtaf2026.common.registry.BlockRegistry;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.profiler.Profilers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderSleepOverlay(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V", shift = At.Shift.AFTER), method = "render")
	private void dtaf2025$render(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
		this.renderSleepPortalOverlay(context, tickCounter);
	}

	@Unique
	private void renderSleepPortalOverlay(DrawContext context, RenderTickCounter tickCounter) {
		if (ClientData.getMinecraft().player != null) {
			ScreenHelper.setSleepAlpha(MathHelper.lerp(tickCounter.getTickProgress(true), ScreenHelper.getPrevSleepAlpha(), ScreenHelper.getSleepAlpha()));
			if (ScreenHelper.getSleepAlpha() > 0) {
				Profilers.get().push(Data.idOf("sleep").toUnderscoreSeparatedString());
				context.createNewRootLayer();
				context.drawSpriteStretched(RenderPipelines.GUI_TEXTURED, ScreenHelper.getSomniumRealePortalSprite(), 0, 0, context.getScaledWindowWidth(), context.getScaledWindowHeight(), ((int)(Math.max(0, Math.min(ScreenHelper.getSleepAlpha() / 100.0F, 1.0F)) * 255.0F) << 24) | 0xFFFFFF);
				Profilers.get().pop();
			}
		}
	}
}
