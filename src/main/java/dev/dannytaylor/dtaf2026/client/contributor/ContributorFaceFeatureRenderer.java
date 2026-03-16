/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.contributor;

import dev.dannytaylor.dtaf2026.client.registry.entity.EntityModelRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.LoadedEntityModels;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;

public class ContributorFaceFeatureRenderer extends FeatureRenderer<PlayerEntityRenderState, PlayerEntityModel> {
	private final ContributorFaceModel<PlayerEntityRenderState> model;

	public ContributorFaceFeatureRenderer(FeatureRendererContext<PlayerEntityRenderState, PlayerEntityModel> context, LoadedEntityModels entityModels) {
		super(context);
		this.model = new ContributorFaceModel<>(entityModels.getModelPart(EntityModelRegistry.contributorFace));
	}

	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, PlayerEntityRenderState state, float limbAngle, float limbDistance) {
		if (!state.invisible) {
			if (((ContributorPlayerEntityRenderState) state).dtaf2026$getBlinking()) {
				this.model.face.copyTransform(this.getContextModel().head);
				this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntitySolid(state.skinTextures.texture())), light, LivingEntityRenderer.getOverlay(state, 0.0F));
			}
		}
	}
}
