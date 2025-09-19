/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.config;

import dev.dannytaylor.dtaf2026.client.config.value.EffectType;
import dev.dannytaylor.dtaf2026.common.data.Data;
import folk.sisby.kaleido.api.ReflectiveConfig;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.FloatRange;
import folk.sisby.kaleido.lib.quiltconfig.api.values.TrackedValue;

import java.nio.file.Paths;

public class Config extends ReflectiveConfig {
	public static final Config instance = Config.createToml(Paths.get("config"), Data.getModId(), "client", Config.class);

	public final TrackedValue<Boolean> skipFlashingLightsWarning = this.value(false);
	public final TrackedValue<EffectType> photosensitiveMode = this.value(EffectType.full);

	@FloatRange(min = 0.0F, max = 1.0F)
	public final TrackedValue<Float> bloomAlpha = this.value(0.5F);

	public final TrackedValue<Boolean> scaleFOV = this.value(true);

	public final TrackedValue<Float> scaleFOVStrength = this.value(0.175F);

	public static void bootstrap() {
	}
}
