/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.mixin.gui;

import dev.dannytaylor.dtaf2026.client.data.ClientData;
import dev.dannytaylor.dtaf2026.client.gui.ScreenHelper;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.SplashTextRenderer;
import net.minecraft.util.Util;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SplashTextRenderer.class)
public abstract class SplashTextRendererMixin {
	@Shadow
	@Final
	private String text;

	@Inject(at = @At("HEAD"), method = "render", cancellable = true)
	private void dtaf2026$render(DrawContext context, int screenWidth, TextRenderer textRenderer, float alpha, CallbackInfo ci) {
		context.getMatrices().pushMatrix();
		context.getMatrices().translate(screenWidth / 2.0F, 79 + ScreenHelper.getTitleYOffset());

		float speed = ClientData.getMinecraft().options.getPanoramaSpeed().getValue().floatValue();
		if (speed > 0.0F) {
			float f = 1.5F - MathHelper.abs(MathHelper.sin((float) (Util.getMeasuringTimeMs() % 2000L) / 2000.0F * (float) (Math.PI * 2)) * (0.1F * speed));
			if (textRenderer.getWidth(this.text) + 32 > 256.0F) f *= 256.0F / (textRenderer.getWidth(this.text) + 32);
			context.getMatrices().scale(f, f);
		}

		context.drawCenteredTextWithShadow(textRenderer, this.text, 0, -8, ColorHelper.withAlpha(alpha, 0xFF55FF));
		context.getMatrices().popMatrix();
		ci.cancel();
	}
}
