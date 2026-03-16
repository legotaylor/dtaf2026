/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry.entity.model;

import dev.dannytaylor.dtaf2026.client.registry.entity.render_state.BoarEntityRenderState;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BabyModelTransformer;
import net.minecraft.client.render.entity.model.ModelTransformer;
import net.minecraft.client.render.entity.model.QuadrupedEntityModel;

import java.util.Set;

public class BoarEntityModel extends QuadrupedEntityModel<BoarEntityRenderState> {
	public static final ModelTransformer babyTransformer = new BabyModelTransformer(false, 4.0F, 4.0F, Set.of("head"));

	public BoarEntityModel(ModelPart modelPart) {
		super(modelPart);
	}

	public static TexturedModelData getTexturedModelData() {
		return getTexturedModelData(Dilation.NONE);
	}

	public static TexturedModelData getTexturedModelData(Dilation dilation) {
		return TexturedModelData.of(getModelData(dilation), 64, 64);
	}

	public static ModelData getModelData(Dilation dilation) {
		int stanceWidth = 6;
		ModelData modelData = QuadrupedEntityModel.getModelData(stanceWidth, true, false, dilation);
		ModelPartData modelPartData = modelData.getRoot();
		modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F, dilation).uv(16, 16).cuboid(-2.0F, 0.0F, -9.0F, 4.0F, 3.0F, 1.0F, dilation), ModelTransform.origin(0.0F, 12.0F, -6.0F));
		ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(28, 8).cuboid(-5.0F, -10.0F, -7.0F, 10.0F, 16.0F, 8.0F, dilation), ModelTransform.of(0.0F, (float) (17 - stanceWidth), 2.0F, ((float) Math.PI / 2F), 0.0F, 0.0F));
		ModelPartData tail = body.addChild("tail", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		tail.addChild("tail1", ModelPartBuilder.create().uv(46, 2).cuboid(1.0F, 0.0F, -4.0F, 0.0F, 1.0F, 5.0F, dilation), ModelTransform.of(-1.0F, 5.25F, -1.0F, 0.2182F, 0.0F, 0.0F));
		return modelData;
	}
}
