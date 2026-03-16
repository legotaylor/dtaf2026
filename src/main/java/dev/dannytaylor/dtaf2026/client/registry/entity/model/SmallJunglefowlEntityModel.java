/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry.entity.model;

import net.minecraft.client.model.*;

public class SmallJunglefowlEntityModel extends AbstractJunglefowlEntityModel {
	public SmallJunglefowlEntityModel(ModelPart root) {
		super(root);
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create().uv(24, 0).cuboid(-2.0F, -4.0F, -2.5F, 4.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.origin(0.0F, 15.0F, -3.5F));
		head.addChild("comb", ModelPartBuilder.create().uv(24, 24).cuboid(0.0F, -2.0F, -2.5F, 0.0F, 4.0F, 5.0F, new Dilation(0.0F)), ModelTransform.origin(0.0F, -4.0F, 0.0F));
		head.addChild("beak", ModelPartBuilder.create().uv(24, 9).cuboid(-2.0F, -1.0F, -1.0F, 4.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.origin(0.0F, -1.0F, -3.5F));
		head.addChild("red_thing", ModelPartBuilder.create().uv(0, 26).cuboid(-1.0F, 3.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.origin(0.0F, -3.0F, -2.5F));
		ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, -4.0F, -5.0F, 6.0F, 8.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 15.0F, 0.0F, 1.6799F, 0.0F, 0.0F));
		body.addChild("tail", ModelPartBuilder.create().uv(0, 14).cuboid(0.0F, -3.0F, -5.0F, 0.0F, 6.0F, 6.0F, new Dilation(0.0F)), ModelTransform.origin(0.0F, 4.0F, 3.0F));
		modelPartData.addChild("left_wing", ModelPartBuilder.create().uv(12, 14).cuboid(0.0F, 1.0F, -3.0F, 1.0F, 4.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(3.0F, 14.0F, 0.0F, 0.1091F, 0.0F, 0.0F));
		modelPartData.addChild("right_wing", ModelPartBuilder.create().uv(12, 14).cuboid(-1.0F, 1.0F, -3.0F, 1.0F, 4.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(-3.0F, 14.0F, 0.0F, 0.1091F, 0.0F, 0.0F));
		modelPartData.addChild("left_leg", ModelPartBuilder.create().uv(12, 24).cuboid(-1.5F, -1.0F, -3.0F, 3.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.origin(1.5F, 19.0F, 1.0F));
		modelPartData.addChild("right_leg", ModelPartBuilder.create().uv(12, 24).cuboid(-1.5F, -1.0F, -3.0F, 3.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.origin(-1.5F, 19.0F, 1.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}
}
