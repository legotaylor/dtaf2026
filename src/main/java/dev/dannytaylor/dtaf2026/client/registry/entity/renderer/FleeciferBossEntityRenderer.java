/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry.entity.renderer;

import dev.dannytaylor.dtaf2026.client.data.ClientData;
import dev.dannytaylor.dtaf2026.client.registry.entity.EntityModelRegistry;
import dev.dannytaylor.dtaf2026.client.registry.entity.model.FleeciferEntityModel;
import dev.dannytaylor.dtaf2026.client.registry.entity.render_state.FleeciferRenderState;
import dev.dannytaylor.dtaf2026.client.registry.entity.renderer.feature.FleeciferWoolFeatureRenderer;
import dev.dannytaylor.dtaf2026.client.registry.entity.renderer.feature.FleeciferWoolUndercoatFeatureRenderer;
import dev.dannytaylor.dtaf2026.common.data.Data;
import dev.dannytaylor.dtaf2026.common.registry.entity.fleecifer.FleeciferBossEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.GuardianEntityRenderer;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class FleeciferBossEntityRenderer extends MobEntityRenderer<FleeciferBossEntity, FleeciferRenderState, FleeciferEntityModel> {
	private static final Identifier texture = Data.idOf("textures/entity/fleecifer/fleecifer.png");
	private static final Identifier beamTexture = Identifier.ofVanilla("textures/entity/guardian_beam.png");
	private static final RenderLayer layer;

	static {
		layer = RenderLayer.getEntityCutoutNoCull(beamTexture);
	}

	public FleeciferBossEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new FleeciferEntityModel(context.getPart(EntityModelRegistry.fleecifer)), 0.7F);
		this.addFeature(new FleeciferWoolUndercoatFeatureRenderer(this, context.getEntityModels()));
		this.addFeature(new FleeciferWoolFeatureRenderer(this, context.getEntityModels()));
	}

	@Nullable
	private static Entity getBeamTarget(FleeciferBossEntity fleecifer) {
		Entity entity = MinecraftClient.getInstance().getCameraEntity();
		return fleecifer.hasBeamTarget() ? fleecifer.getBeamTarget() : entity;
	}

	public Identifier getTexture(FleeciferRenderState sheepEntityRenderState) {
		return texture;
	}

	public FleeciferRenderState createRenderState() {
		return new FleeciferRenderState();
	}

	public void updateRenderState(FleeciferBossEntity fleecifer, FleeciferRenderState renderState, float f) {
		super.updateRenderState(fleecifer, renderState, f);
		renderState.id = fleecifer.getId();
		renderState.isBeaming = FleeciferBossEntity.isBeaming(fleecifer);
		renderState.eyePosVec = fleecifer.getEyePos();
		renderState.beamProgress = fleecifer.getBeamProgress(f);
		renderState.beamTicks = fleecifer.getBeamTicks() + f;
		renderState.beamTargetPos = getBeamTargetPos(fleecifer, f);
		renderState.rainbow = true;
	}

	@Nullable
	private Vec3d getBeamTargetPos(FleeciferBossEntity fleecifer, float f) {
		Entity entity = getBeamTarget(fleecifer);
		return entity != null ? this.fromLerpedPosition(entity, (double) entity.getHeight() * (double) 0.5F, f) : null;
	}

	public void render(FleeciferRenderState fleeciferBossRenderState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
		super.render(fleeciferBossRenderState, matrixStack, vertexConsumerProvider, i);
		if (fleeciferBossRenderState.isBeaming) {
			Vec3d vec3d = fleeciferBossRenderState.beamTargetPos;
			if (vec3d == null && ClientData.getMinecraft().cameraEntity != null)
				vec3d = ClientData.getMinecraft().cameraEntity.getCameraPosVec(1.0F);
			if (vec3d != null) {
				float f = fleeciferBossRenderState.beamTicks * 0.5F % 1.0F;
				matrixStack.push();
				matrixStack.translate(0.0F, fleeciferBossRenderState.standingEyeHeight, 0.0F);
				GuardianEntityRenderer.renderBeam(matrixStack, vertexConsumerProvider.getBuffer(layer), vec3d.subtract(fleeciferBossRenderState.eyePosVec), fleeciferBossRenderState.beamTicks, fleeciferBossRenderState.beamProgress, f);
				matrixStack.pop();
			}
		}
	}

	private Vec3d fromLerpedPosition(Entity entity, double yOffsetMulti, float delta) {
		if (entity instanceof LivingEntity livingEntity) {
			double d = MathHelper.lerp(delta, livingEntity.lastRenderX, entity.getX());
			double e = MathHelper.lerp(delta, livingEntity.lastRenderY, entity.getY()) + (livingEntity.getHeight() * yOffsetMulti);
			double f = MathHelper.lerp(delta, livingEntity.lastRenderZ, entity.getZ());
			return new Vec3d(d, e, f);
		} else return null;
	}
}
