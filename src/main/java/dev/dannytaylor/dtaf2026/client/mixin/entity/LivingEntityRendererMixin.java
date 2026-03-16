/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.mixin.entity;

import dev.dannytaylor.dtaf2026.client.contributor.Contributor;
import dev.dannytaylor.dtaf2026.client.data.ClientData;
import dev.dannytaylor.dtaf2026.client.registry.keybinds.HoldPerspective;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {
	@Inject(method = "shouldFlipUpsideDown", at = @At("RETURN"), cancellable = true)
	private static void dtaf2026$updateRenderState(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
		if (entity instanceof PlayerEntity player && Contributor.isLegoTaylor(player.getUuidAsString()) && !(player.equals(ClientData.getMinecraft().cameraEntity) && HoldPerspective.wasFrontPressed))
			cir.setReturnValue(!cir.getReturnValue());
	}
}
