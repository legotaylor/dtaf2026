/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.contributor;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;

public class ContributorFaceModel<S extends PlayerEntityRenderState> extends EntityModel<S> {
	public final ModelPart face;

	public ContributorFaceModel(ModelPart root) {
		super(root, RenderLayer::getEntityTranslucent);
		this.face = root.getChild("face");
	}

	public static TexturedModelData getTexturedModelData(Dilation dilation) {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		modelPartData.addChild("face", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.001F, 8.0F, 8.0F, 0.0F, dilation), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 24.0F, 0.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}
}
