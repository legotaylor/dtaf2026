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

@Environment(EnvType.CLIENT)
public class SomniumRealeUBO extends UBOSettings {
	private float light;
	private float skyLight;
	private float blockLight;
	private float bloomAlpha;
	private float gameTime;
	private float worldTime;
	private float tickProgress;
	private float prevLight;
	private float prevSkyLight;
	private float prevBlockLight;
	private float prevBloomAlpha;
	private float prevGameTime;

	public SomniumRealeUBO(String name) {
		super(name, new Std140Builder().putVec4().putVec4().putVec4().putVec4());
	}

	public void set() {
		float smoothLight = smooth(this.prevLight, this.light);
		float smoothSkyLight = smooth(this.prevSkyLight, this.skyLight);
		float smoothBlockLight = smooth(this.prevBlockLight, this.blockLight);
		float smoothBloomAlpha = smooth(this.prevBloomAlpha, this.bloomAlpha);
		float smoothGameTime = smooth(this.prevGameTime, this.gameTime);

		this.set(new Vector4f(this.light, this.skyLight, this.blockLight, Config.instance.photosensitiveMode.value().getNumericId()),
			new Vector4f(smoothLight, smoothSkyLight, smoothBlockLight, 0),
			new Vector4f(this.bloomAlpha, smoothBloomAlpha, 0, 0),
			new Vector4f(this.gameTime, smoothGameTime, this.worldTime, this.tickProgress)
		);
	}

	public void update(float light, float skyLight, float blockLight, float bloomAlpha, long time) {
		float worldTime = time % 24000L;
		float tickProgress = ClientData.getMinecraft().getRenderTickCounter().getTickProgress(false);
		float gameTime = (worldTime + tickProgress) / 24000.0F;

		this.prevLight = this.light;
		this.prevSkyLight = this.skyLight;
		this.prevBlockLight = this.blockLight;
		this.prevBloomAlpha = this.bloomAlpha;
		this.prevGameTime = this.gameTime;

		this.light = light / 15;
		this.skyLight = skyLight / 15;
		this.blockLight = blockLight / 15;
		this.bloomAlpha = bloomAlpha;
		this.gameTime = gameTime;
		this.worldTime = worldTime;
		this.tickProgress = tickProgress;
	}

	public void apply() {
		super.apply();
	}

	private float smooth(float prevValue, float value) {
		return MathHelper.lerp(ClientData.getMinecraft().getRenderTickCounter().getTickProgress(true), prevValue, value);
	}
}
