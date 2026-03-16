/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.mixin.entity;

import dev.dannytaylor.dtaf2026.client.registry.entity.render_state.CreakingVariantEntityRenderState;
import dev.dannytaylor.dtaf2026.common.registry.entity.creaking.TerrorlandsCreaking;
import net.minecraft.client.render.entity.CreakingEntityRenderer;
import net.minecraft.client.render.entity.state.CreakingEntityRenderState;
import net.minecraft.entity.mob.CreakingEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CreakingEntityRenderer.class)
public abstract class CreakingEntityRendererMixin<T extends CreakingEntity> {
	@Inject(at = @At("RETURN"), method = "updateRenderState(Lnet/minecraft/entity/mob/CreakingEntity;Lnet/minecraft/client/render/entity/state/CreakingEntityRenderState;F)V")
	private void dtaf2026$updateRenderState(T creakingEntity, CreakingEntityRenderState renderState, float f, CallbackInfo ci) {
		((CreakingVariantEntityRenderState) renderState).dtaf2026$setVariant(((TerrorlandsCreaking) creakingEntity).dtaf2026$getVariant());
	}

	@Inject(at = @At("RETURN"), method = "getTexture(Lnet/minecraft/client/render/entity/state/CreakingEntityRenderState;)Lnet/minecraft/util/Identifier;", cancellable = true)
	private void dtaf2026$getTexture(CreakingEntityRenderState renderState, CallbackInfoReturnable<Identifier> cir) {
		Identifier variant = ((CreakingVariantEntityRenderState) renderState).dtaf2026$getVariant();
		cir.setReturnValue(Identifier.of(variant.getNamespace(), "textures/entity/creaking/" + variant.getPath() + ".png"));
	}
}
