/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry.entity.renderer;

import com.google.common.collect.Maps;
import dev.dannytaylor.dtaf2026.client.registry.entity.EntityModelRegistry;
import dev.dannytaylor.dtaf2026.client.registry.entity.model.BoarEntityModel;
import dev.dannytaylor.dtaf2026.client.registry.entity.render_state.BoarEntityRenderState;
import dev.dannytaylor.dtaf2026.common.registry.entity.boar.BoarEntity;
import dev.dannytaylor.dtaf2026.common.registry.entity.boar.BoarVariant;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.BabyModelPair;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.equipment.EquipmentModel;
import net.minecraft.client.render.entity.feature.SaddleFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PigEntityModel;
import net.minecraft.client.texture.MissingSprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class BoarEntityRenderer extends MobEntityRenderer<BoarEntity, BoarEntityRenderState, BoarEntityModel> {
	private final Map<BoarVariant.Model, BabyModelPair<BoarEntityModel>> modelPairs;

	public BoarEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new BoarEntityModel(context.getPart(EntityModelRegistry.boar)), 0.7F);
		this.modelPairs = createModelPairs(context);
		this.addFeature(new SaddleFeatureRenderer<>(this, context.getEquipmentRenderer(), EquipmentModel.LayerType.PIG_SADDLE, (boarEntityRenderState) -> boarEntityRenderState.saddleStack, new PigEntityModel(context.getPart(EntityModelLayers.PIG_SADDLE)), new PigEntityModel(context.getPart(EntityModelLayers.PIG_BABY_SADDLE))));
	}

	private static Map<BoarVariant.Model, BabyModelPair<BoarEntityModel>> createModelPairs(EntityRendererFactory.Context context) {
		return Maps.newEnumMap(Map.of(BoarVariant.Model.temperate, new BabyModelPair<>(new BoarEntityModel(context.getPart(EntityModelRegistry.boar)), new BoarEntityModel(context.getPart(EntityModelRegistry.babyBoar)))));
	}

	public void render(BoarEntityRenderState renderState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
		if (renderState.variant != null) {
			this.model = this.modelPairs.get(renderState.variant.modelAndTexture().model()).get(renderState.baby);
		}
		super.render(renderState, matrixStack, vertexConsumerProvider, i);
	}

	public Identifier getTexture(BoarEntityRenderState boarEntityRenderState) {
		return boarEntityRenderState.variant == null ? MissingSprite.getMissingSpriteId() : boarEntityRenderState.variant.modelAndTexture().asset().texturePath();
	}

	public BoarEntityRenderState createRenderState() {
		return new BoarEntityRenderState();
	}

	public void updateRenderState(BoarEntity boarEntity, BoarEntityRenderState boarEntityRenderState, float f) {
		super.updateRenderState(boarEntity, boarEntityRenderState, f);
		boarEntityRenderState.saddleStack = boarEntity.getEquippedStack(EquipmentSlot.SADDLE).copy();
		boarEntityRenderState.variant = boarEntity.getVariantData();
	}
}
