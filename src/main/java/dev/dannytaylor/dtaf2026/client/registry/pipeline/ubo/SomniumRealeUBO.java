/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry.pipeline.ubo;

import dev.dannytaylor.dtaf2026.client.data.ClientData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector4f;
import org.joml.Vector4i;

@Environment(EnvType.CLIENT)
public class SomniumRealeUBO extends UBOSettings {
	private int prevLight;
	private int prevSkyLight;
	private int prevBlockLight;
	private float prevBloomAlpha;

	public SomniumRealeUBO(String name) {
		super(name, new Std140Builder().putIVec4().putVec4().putVec4());
	}

	public void set(int light, int skyLight, int blockLight, float bloomAlpha) {
		this.set(new Vector4i(light, skyLight, blockLight, 0),
			new Vector4f(softSmooth(prevLight, light), softSmooth(prevSkyLight, skyLight), softSmooth(prevBlockLight, blockLight), 0),
			new Vector4f(bloomAlpha, softSmooth(prevBloomAlpha, bloomAlpha), 0, 0));
		this.prevLight = light;
		this.prevSkyLight = skyLight;
		this.prevBlockLight = blockLight;
		this.prevBloomAlpha = bloomAlpha;
	}

	public void apply() {
		super.apply();
	}

	private float softSmooth(float prev, float current) {
		float softened = (prev + current) * 0.5F;
		return smooth(prev, softened);
	}

	private float smooth(float prevValue, float value) {
		return MathHelper.lerp(ClientData.getMinecraft().getRenderTickCounter().getTickProgress(true), prevValue, value);
	}
}
