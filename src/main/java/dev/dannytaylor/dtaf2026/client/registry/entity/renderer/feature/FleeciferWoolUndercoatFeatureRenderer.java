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
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.LoadedEntityModels;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class FleeciferWoolUndercoatFeatureRenderer extends FeatureRenderer<FleeciferRenderState, FleeciferEntityModel> {
	private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/sheep/sheep_wool_undercoat.png");
	private final EntityModel<FleeciferRenderState> model;

	public FleeciferWoolUndercoatFeatureRenderer(FeatureRendererContext<FleeciferRenderState, FleeciferEntityModel> context, LoadedEntityModels loader) {
		super(context);
		this.model = new FleeciferWoolEntityModel(loader.getModelPart(EntityModelRegistry.fleeciferWoolUndercoat));
	}

	public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, FleeciferRenderState FleeciferEntityRenderState, float f, float g) {
		if (!FleeciferEntityRenderState.invisible) {
			render(this.model, TEXTURE, matrixStack, vertexConsumerProvider, i, FleeciferEntityRenderState, FleeciferEntityRenderState.getRgbColor());
		}
	}
}
