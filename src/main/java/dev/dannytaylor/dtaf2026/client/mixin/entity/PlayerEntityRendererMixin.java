/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.mixin.entity;

import dev.dannytaylor.dtaf2026.client.contributor.ContributorFaceFeatureRenderer;
import dev.dannytaylor.dtaf2026.client.contributor.ContributorPlayerEntity;
import dev.dannytaylor.dtaf2026.client.contributor.ContributorPlayerEntityRenderState;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityRenderState, PlayerEntityModel> {
	public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel model, float shadowRadius) {
		super(ctx, model, shadowRadius);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void dtaf2026$init(EntityRendererFactory.Context context, boolean slim, CallbackInfo ci) {
		this.addFeature(new ContributorFaceFeatureRenderer(this, context.getEntityModels()));
	}

	@Inject(method = "updateRenderState(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;F)V", at = @At("TAIL"))
	private void dtaf2026$updateRenderState(AbstractClientPlayerEntity player, PlayerEntityRenderState playerRenderState, float tickDelta, CallbackInfo ci) {
		ContributorPlayerEntityRenderState renderState = (ContributorPlayerEntityRenderState) playerRenderState;
		ContributorPlayerEntity contributor = (ContributorPlayerEntity) player;
		renderState.dtaf2026$setBlinking(contributor.dtaf2026$getBlinking());
	}
}
