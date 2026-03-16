/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry.entity.renderer.feature;

import dev.dannytaylor.dtaf2026.client.registry.entity.EntityModelRegistry;
import dev.dannytaylor.dtaf2026.client.registry.entity.model.FleeciferEntityModel;
import dev.dannytaylor.dtaf2026.client.registry.entity.model.FleeciferWoolEntityModel;
import dev.dannytaylor.dtaf2026.client.registry.entity.render_state.FleeciferRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.LoadedEntityModels;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class FleeciferWoolFeatureRenderer extends FeatureRenderer<FleeciferRenderState, FleeciferEntityModel> {
	private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/sheep/sheep_wool.png");
	private final EntityModel<FleeciferRenderState> woolModel;

	public FleeciferWoolFeatureRenderer(FeatureRendererContext<FleeciferRenderState, FleeciferEntityModel> context, LoadedEntityModels loader) {
		super(context);
		this.woolModel = new FleeciferWoolEntityModel(loader.getModelPart(EntityModelRegistry.fleeciferWool));
	}

	public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, FleeciferRenderState fleeciferBossRenderState, float f, float g) {
		if (fleeciferBossRenderState.invisible) {
			if (fleeciferBossRenderState.hasOutline) {
				this.woolModel.setAngles(fleeciferBossRenderState);
				VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getOutline(TEXTURE));
				this.woolModel.render(matrixStack, vertexConsumer, i, LivingEntityRenderer.getOverlay(fleeciferBossRenderState, 0.0F), -16777216);
			}
		} else {
			render(this.woolModel, TEXTURE, matrixStack, vertexConsumerProvider, i, fleeciferBossRenderState, fleeciferBossRenderState.getRgbColor());
		}
	}
}
