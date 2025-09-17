/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.mixin.gui;

import dev.dannytaylor.dtaf2026.client.gui.ScreenHelper;
import net.minecraft.client.gui.screen.SplashTextRenderer;
import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SplashTextRenderer.class)
public abstract class SplashTextRendererMixin {
	@Redirect(at = @At(value = "INVOKE", target = "Lorg/joml/Matrix3x2fStack;translate(FF)Lorg/joml/Matrix3x2f;"), method = "render", remap = false)
	private Matrix3x2f dtaf2026$updateY(Matrix3x2fStack instance, float x, float y) {
		return instance.translate(x, y + ScreenHelper.getTitleYOffset());
	}
}
