/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry.entity.render_state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.ColorLerper;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class FleeciferRenderState extends LivingEntityRenderState {
	public int id;
	public Vec3d eyePosVec;
	public boolean isBeaming;
	@Nullable
	public Vec3d beamTargetPos;
	public float beamTicks;
	public float beamProgress;
	public boolean rainbow;

	public FleeciferRenderState() {
		this.eyePosVec = Vec3d.ZERO;
	}

	public int getRgbColor() {
		return this.rainbow ? ColorLerper.lerpColor(ColorLerper.Type.SHEEP, this.age * 2) : ColorLerper.Type.SHEEP.getArgb(DyeColor.WHITE);
	}
}
