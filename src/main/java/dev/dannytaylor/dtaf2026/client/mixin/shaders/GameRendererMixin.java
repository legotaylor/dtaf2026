/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.mixin.shaders;

import dev.dannytaylor.dtaf2026.client.data.ClientData;
import dev.dannytaylor.dtaf2026.client.registry.pipeline.PostEffectRegistry;
import dev.dannytaylor.dtaf2026.client.registry.pipeline.ubo.UBORegistry;
import dev.dannytaylor.dtaf2026.common.data.Data;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/GlobalSettings;set(IIDJLnet/minecraft/client/render/RenderTickCounter;I)V", shift = At.Shift.AFTER), method = "render")
	private void dtaf2026$render(CallbackInfo ci) {
		UBORegistry.apply();
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/GlobalSettings;close()V", shift = At.Shift.AFTER), method = "close")
	private void dtaf2026$close(CallbackInfo ci) {
		UBORegistry.close();
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/GlobalSettings;<init>()V", shift = At.Shift.AFTER), method = "<init>")
	private void dtaf2026$init(CallbackInfo ci) {
		UBORegistry.bootstrap();
	}

	@ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/ShaderLoader;loadPostEffect(Lnet/minecraft/util/Identifier;Ljava/util/Set;)Lnet/minecraft/client/gl/PostEffectProcessor;"), method = "renderBlur")
	private Identifier dtaf2026$changeBlurShader(Identifier id) {
		return Data.idOf("blur");
	}

	@Inject(at = @At("RETURN"), method = "close")
	private void dtaf2026$shaders_close(CallbackInfo ci) {
		for (PostEffectRegistry.PostEffect postEffect : PostEffectRegistry.postEffects) postEffect.close();
	}

	@Inject(at = @At("RETURN"), method = "onResized")
	private void dtaf2026$shaders_onResized(int width, int height, CallbackInfo ci) {
		for (PostEffectRegistry.PostEffect postEffect : PostEffectRegistry.postEffects) postEffect.onResized();
	}

	@Unique
	private static float dtaf2026$nightVisionStrength = 0.0F;

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;drawEntityOutlinesFramebuffer()V", shift = At.Shift.AFTER), method = "render")
	private void dtaf2026$shaders_render(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
		for (PostEffectRegistry.PostEffect postEffect : PostEffectRegistry.postEffects)
			if (postEffect.getType().equals(PostEffectRegistry.PostEffect.Type.GAME))
				postEffect.render(ClientData.getMinecraft());
	}

	@Inject(at = @At("HEAD"), method = "getNightVisionStrength", cancellable = true)
	private static void dtaf2026$getNightVisionStrength(LivingEntity entity, float tickProgress, CallbackInfoReturnable<Float> cir) {
		cir.setReturnValue(dtaf2026$nightVisionStrength = MathHelper.lerp(tickProgress, dtaf2026$nightVisionStrength, entity.getStatusEffect(StatusEffects.NIGHT_VISION) != null ? 1.0F : 0.0F));
	}
}
