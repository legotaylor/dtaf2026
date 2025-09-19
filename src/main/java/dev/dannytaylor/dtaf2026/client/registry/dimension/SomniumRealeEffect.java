/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry.dimension;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
public class SomniumRealeEffect extends DimensionEffects {
	public SomniumRealeEffect() {
		super(DimensionEffects.SkyType.NORMAL, true, true);
	}

	public boolean isSunRisingOrSetting(float skyAngle) {
		float f = MathHelper.cos(skyAngle * ((float)Math.PI * 2F));
		return f >= -0.4F && f <= 0.4F;
	}

	public int getSkyColor(float skyAngle) {
		float f = MathHelper.cos(skyAngle * ((float)Math.PI * 2F));
		float g = f / 0.4F * 0.5F + 0.5F;
		float h = MathHelper.square(1.0F - (1.0F - MathHelper.sin(g * (float)Math.PI)) * 0.99F);
		return ColorHelper.fromFloats(h, g * 0.3F + 0.7F, g * g * 0.7F + 0.2F, 0.2F);
	}

	public Vec3d adjustFogColor(Vec3d color, float sunHeight) {
		return color.multiply(sunHeight * 0.94F + 0.06F, sunHeight * 0.94F + 0.06F, sunHeight * 0.91F + 0.09F);
	}

	public boolean useThickFog(int camX, int camY) {
		return false;
	}
}
