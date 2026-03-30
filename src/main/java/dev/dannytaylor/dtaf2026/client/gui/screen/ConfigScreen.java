/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.gui.screen;

import com.mojang.serialization.Codec;
import dev.dannytaylor.dtaf2026.client.config.Config;
import dev.dannytaylor.dtaf2026.client.config.value.EffectType;
import dev.dannytaylor.dtaf2026.client.data.ClientData;
import dev.dannytaylor.dtaf2026.common.data.Data;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;

import java.util.Arrays;

@Environment(EnvType.CLIENT)
public class ConfigScreen extends GameOptionsScreen {
	private static final SimpleOption<Double> bloomAlpha = new SimpleOption<>("options.dtaf2026.bloom_alpha",
		SimpleOption.emptyTooltip(),
		(prefix, value) -> Text.translatable("options.percent_value", prefix, (int) (value * 100.0)),
		SimpleOption.DoubleSliderCallbacks.INSTANCE,
		(double) Config.instance.bloomAlpha.value(),
		value -> Config.instance.bloomAlpha.setValue(value.floatValue(), false)
	);

	private static final SimpleOption<EffectType> photosensitiveMode = new SimpleOption<>("options.dtaf2026.photosensitive_mode",
		value -> value.equals(EffectType.full) ? null : Tooltip.of(Text.translatable("options.dtaf2026.photosensitive_mode." + value.getId() + ".tooltip")),
		(prefix, value) -> Text.translatable("options.dtaf2026.photosensitive_mode." + value.getId(), prefix),
		new SimpleOption.PotentialValuesBasedCallbacks<>(Arrays.asList(EffectType.values()), Codec.INT.xmap(EffectType::byId, EffectType::getNumericId)),
		Config.instance.photosensitiveMode.value(),
		value -> Config.instance.photosensitiveMode.setValue(value, false)
	);

	public ConfigScreen(Screen parent) {
		super(parent, ClientData.getMinecraft().options, Data.getText("name"));
	}

	private static SimpleOption<?>[] getOptions() {
		return new SimpleOption[]{
			bloomAlpha,
			photosensitiveMode
		};
	}

	@Override
	protected void addOptions() {
		if (this.body != null) this.body.addAll(getOptions());
	}

	@Override
	public void close() {
		Config.instance.save();
		super.close();
	}
}
