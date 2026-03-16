/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry.entity.renderer;

import com.google.common.collect.Maps;
import dev.dannytaylor.dtaf2026.client.registry.entity.EntityModelRegistry;
import dev.dannytaylor.dtaf2026.client.registry.entity.model.AbstractJunglefowlEntityModel;
import dev.dannytaylor.dtaf2026.client.registry.entity.model.BigJunglefowlEntityModel;
import dev.dannytaylor.dtaf2026.client.registry.entity.model.SmallJunglefowlEntityModel;
import dev.dannytaylor.dtaf2026.client.registry.entity.render_state.JunglefowlEntityRenderState;
import dev.dannytaylor.dtaf2026.common.registry.entity.junglefowl.JunglefowlEntity;
import dev.dannytaylor.dtaf2026.common.registry.entity.junglefowl.JunglefowlVariant;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.BabyModelPair;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.texture.MissingSprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class JunglefowlEntityRenderer extends MobEntityRenderer<JunglefowlEntity, JunglefowlEntityRenderState, AbstractJunglefowlEntityModel> {
	private final Map<JunglefowlVariant.Model, BabyModelPair<AbstractJunglefowlEntityModel>> babyModelPairMap;

	public JunglefowlEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new BigJunglefowlEntityModel(context.getPart(EntityModelRegistry.bigJunglefowl)), 0.45F);
		this.babyModelPairMap = createBabyModelPairMap(context);
	}

	private static Map<JunglefowlVariant.Model, BabyModelPair<AbstractJunglefowlEntityModel>> createBabyModelPairMap(EntityRendererFactory.Context context) {
		return Maps.newEnumMap(Map.of(JunglefowlVariant.Model.big, new BabyModelPair<>(new BigJunglefowlEntityModel(context.getPart(EntityModelRegistry.bigJunglefowl)), new BigJunglefowlEntityModel(context.getPart(EntityModelRegistry.babyBigJunglefowl))), JunglefowlVariant.Model.small, new BabyModelPair<>(new SmallJunglefowlEntityModel(context.getPart(EntityModelRegistry.smallJunglefowl)), new SmallJunglefowlEntityModel(context.getPart(EntityModelRegistry.babySmallJunglefowl)))));
	}

	public void render(JunglefowlEntityRenderState junglefowlEntityRenderState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
		if (junglefowlEntityRenderState.variant != null) {
			this.model = this.babyModelPairMap.get(junglefowlEntityRenderState.variant.modelAndTextureAndTexture().model()).get(junglefowlEntityRenderState.baby);
			super.render(junglefowlEntityRenderState, matrixStack, vertexConsumerProvider, i);
		}
	}

	public Identifier getTexture(JunglefowlEntityRenderState junglefowlEntityRenderState) {
		return junglefowlEntityRenderState.variant == null ? MissingSprite.getMissingSpriteId() : (junglefowlEntityRenderState.baby ? junglefowlEntityRenderState.variant.modelAndTextureAndTexture().babyAsset() : junglefowlEntityRenderState.variant.modelAndTextureAndTexture().asset()).texturePath();
	}

	public JunglefowlEntityRenderState createRenderState() {
		return new JunglefowlEntityRenderState();
	}

	public void updateRenderState(JunglefowlEntity JunglefowlEntity, JunglefowlEntityRenderState junglefowlEntityRenderState, float f) {
		super.updateRenderState(JunglefowlEntity, junglefowlEntityRenderState, f);
		junglefowlEntityRenderState.flapProgress = MathHelper.lerp(f, JunglefowlEntity.lastFlapProgress, JunglefowlEntity.flapProgress);
		junglefowlEntityRenderState.maxWingDeviation = MathHelper.lerp(f, JunglefowlEntity.lastMaxWingDeviation, JunglefowlEntity.maxWingDeviation);
		junglefowlEntityRenderState.variant = JunglefowlEntity.getVariantData();
	}
}
