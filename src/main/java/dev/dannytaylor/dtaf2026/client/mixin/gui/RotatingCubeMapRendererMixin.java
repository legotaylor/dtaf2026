/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.mixin.gui;

import dev.dannytaylor.dtaf2026.client.data.ClientData;
import dev.dannytaylor.dtaf2026.client.registry.pipeline.PostEffectRegistry;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class RotatingCubeMapRendererMixin {
	@Inject(method = "renderPanoramaBackground", at = @At("RETURN"))
	private void dtaf2026$render(DrawContext context, float deltaTicks, CallbackInfo ci) {
		for (PostEffectRegistry.PostEffect postEffect : PostEffectRegistry.postEffects) {
			if (postEffect.getType().equals(PostEffectRegistry.PostEffect.Type.PANORAMA))
				postEffect.render(ClientData.getMinecraft());
		}
	}
}
