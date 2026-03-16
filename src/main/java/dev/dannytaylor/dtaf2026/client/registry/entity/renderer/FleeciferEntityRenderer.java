/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry.entity.renderer;

import dev.dannytaylor.dtaf2026.client.registry.entity.EntityModelRegistry;
import dev.dannytaylor.dtaf2026.client.registry.entity.model.FleeciferEntityModel;
import dev.dannytaylor.dtaf2026.client.registry.entity.render_state.FleeciferRenderState;
import dev.dannytaylor.dtaf2026.client.registry.entity.renderer.feature.FleeciferWoolFeatureRenderer;
import dev.dannytaylor.dtaf2026.client.registry.entity.renderer.feature.FleeciferWoolUndercoatFeatureRenderer;
import dev.dannytaylor.dtaf2026.common.data.Data;
import dev.dannytaylor.dtaf2026.common.registry.entity.fleecifer.FleeciferEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
public class FleeciferEntityRenderer extends MobEntityRenderer<FleeciferEntity, FleeciferRenderState, FleeciferEntityModel> {
	private static final Identifier texture = Data.idOf("textures/entity/fleecifer/fleecifer.png");

	public FleeciferEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new FleeciferEntityModel(context.getPart(EntityModelRegistry.fleecifer)), 0.7F);
		this.addFeature(new FleeciferWoolUndercoatFeatureRenderer(this, context.getEntityModels()));
		this.addFeature(new FleeciferWoolFeatureRenderer(this, context.getEntityModels()));
	}

	public Identifier getTexture(FleeciferRenderState sheepEntityRenderState) {
		return texture;
	}

	public FleeciferRenderState createRenderState() {
		return new FleeciferRenderState();
	}

	public void updateRenderState(FleeciferEntity fleecifer, FleeciferRenderState renderState, float f) {
		super.updateRenderState(fleecifer, renderState, f);
		renderState.id = fleecifer.getId();
		renderState.isBeaming = false;
		renderState.eyePosVec = fleecifer.getEyePos();
		renderState.beamProgress = 0.0F;
		renderState.beamTicks = 0.0F;
		renderState.beamTargetPos = Vec3d.ZERO;
		renderState.rainbow = fleecifer.isRainbow();
	}
}
