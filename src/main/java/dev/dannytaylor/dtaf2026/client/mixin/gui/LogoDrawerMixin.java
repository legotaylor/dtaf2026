/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.mixin.gui;

import com.llamalad7.mixinextras.sugar.Local;
import dev.dannytaylor.dtaf2026.client.gui.TitleScreenHelper;
import dev.dannytaylor.dtaf2026.common.data.Data;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.LogoDrawer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(LogoDrawer.class)
public abstract class LogoDrawerMixin {
	@ModifyArgs(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/LogoDrawer;draw(Lnet/minecraft/client/gui/DrawContext;IFI)V"), method = "draw(Lnet/minecraft/client/gui/DrawContext;IF)V")
	private void dtaf2026$updateY(Args args) {
		args.set(args.size() - 1, (int)args.get(args.size() - 1) + TitleScreenHelper.getTitleYOffset());
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIFFIIIII)V", ordinal = 0), method = "draw(Lnet/minecraft/client/gui/DrawContext;IFI)V")
	private void dtaf2026$addUpdateTexture(DrawContext context, int screenWidth, float alpha, int y, CallbackInfo ci, @Local(name = "j") int color) {
		context.drawTexture(RenderPipelines.GUI_TEXTURED, Data.idOf("textures/gui/title/subtitle.png"), screenWidth / 2 - 160, y, 0.0F, 0.0F, 320, 256, 320, 256, color);
	}
}
