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
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
public class SomniumRealeEffect extends DimensionEffects {
	public SomniumRealeEffect() {
		super(DimensionEffects.SkyType.NORMAL, true, true);
	}

	public Vec3d adjustFogColor(Vec3d color, float sunHeight) {
		return color.multiply(0.15F);
	}

	public boolean useThickFog(int camX, int camY) {
		return false;
	}
}
