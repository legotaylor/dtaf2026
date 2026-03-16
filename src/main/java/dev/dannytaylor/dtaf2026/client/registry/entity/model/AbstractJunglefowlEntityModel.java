/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry.entity.model;

import dev.dannytaylor.dtaf2026.client.registry.entity.render_state.JunglefowlEntityRenderState;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BabyModelTransformer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelTransformer;
import net.minecraft.util.math.MathHelper;

import java.util.Set;

public abstract class AbstractJunglefowlEntityModel extends EntityModel<JunglefowlEntityRenderState> {
	public static final ModelTransformer babyTransformer = new BabyModelTransformer(false, 5.0f, 2.0f, 2.0f, 1.99f, 24.0f, Set.of("head", "beak", "red_thing", "comb"));

	private final ModelPart head;
	private final ModelPart leftWing;
	private final ModelPart rightWing;
	private final ModelPart leftLeg;
	private final ModelPart rightLeg;

	public AbstractJunglefowlEntityModel(ModelPart root) {
		super(root);
		this.head = root.getChild("head");
		this.leftWing = root.getChild("left_wing");
		this.rightWing = root.getChild("right_wing");
		this.leftLeg = root.getChild("left_leg");
		this.rightLeg = root.getChild("right_leg");
	}

	@Override
	public void setAngles(JunglefowlEntityRenderState junglefowlEntityRenderState) {
		super.setAngles(junglefowlEntityRenderState);
		float roll = (MathHelper.sin(junglefowlEntityRenderState.flapProgress) + 1.0f) * junglefowlEntityRenderState.maxWingDeviation;
		this.head.pitch = junglefowlEntityRenderState.pitch * ((float) Math.PI / 180);
		this.head.yaw = junglefowlEntityRenderState.relativeHeadYaw * ((float) Math.PI / 180);
		float amplitude = junglefowlEntityRenderState.limbSwingAmplitude;
		float progress = junglefowlEntityRenderState.limbSwingAnimationProgress;
		this.rightLeg.pitch = MathHelper.cos(progress * 0.6662f) * 1.4f * amplitude;
		this.leftLeg.pitch = MathHelper.cos(progress * 0.6662f + (float) Math.PI) * 1.4f * amplitude;
		this.rightWing.roll = roll;
		this.leftWing.roll = -roll;
	}
}
