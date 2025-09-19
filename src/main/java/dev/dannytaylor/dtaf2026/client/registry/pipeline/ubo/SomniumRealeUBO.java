/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry.pipeline.ubo;

import dev.dannytaylor.dtaf2026.client.config.Config;
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
	private float prevGameTime;

	public SomniumRealeUBO(String name) {
		super(name, new Std140Builder().putIVec4().putVec4().putVec4().putVec4());
	}

	public void set(int light, int skyLight, int blockLight, float bloomAlpha, long time) {
		float worldTime = time % 24000L;
		float tickProgress = ClientData.getMinecraft().getRenderTickCounter().getTickProgress(false);
		float gameTime = (worldTime + tickProgress) / 24000.0F;
		this.set(new Vector4i(light, skyLight, blockLight, Config.instance.photosensitiveMode.value().getNumericId()),
			new Vector4f(softSmooth(this.prevLight, light), softSmooth(this.prevSkyLight, skyLight), softSmooth(this.prevBlockLight, blockLight), 0),
			new Vector4f(bloomAlpha, softSmooth(this.prevBloomAlpha, bloomAlpha), 0, 0),
			new Vector4f(gameTime, softSmooth(this.prevGameTime, gameTime), worldTime, tickProgress)
		);
		this.prevLight = light;
		this.prevSkyLight = skyLight;
		this.prevBlockLight = blockLight;
		this.prevBloomAlpha = bloomAlpha;
		this.prevGameTime = gameTime;
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
