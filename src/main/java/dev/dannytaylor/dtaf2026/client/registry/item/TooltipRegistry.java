/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry.item;

import dev.dannytaylor.dtaf2026.client.data.ClientData;
import dev.dannytaylor.dtaf2026.client.gui.TextListTooltipComponent;
import dev.dannytaylor.dtaf2026.common.data.Data;
import dev.dannytaylor.dtaf2026.common.registry.item.component.RelicBundleContentsComponent;
import dev.dannytaylor.dtaf2026.common.registry.item.component.RelicComponent;
import dev.dannytaylor.dtaf2026.common.registry.item.tooltip.RelicBundleTooltipData;
import dev.dannytaylor.dtaf2026.common.registry.item.tooltip.RelicTooltipData;
import dev.dannytaylor.dtaf2026.common.registry.relic.Relic;
import dev.dannytaylor.dtaf2026.common.registry.relic.RelicLoader;
import dev.dannytaylor.dtaf2026.common.registry.tagkey.TagRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.minecraft.registry.Registries;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class TooltipRegistry {
	public static void bootstrap() {
		TooltipComponentCallback.EVENT.register(data -> {
			if (data instanceof RelicBundleTooltipData(RelicBundleContentsComponent contents)) {
				Identifier contentsId = contents.isPresent() ? Registries.ITEM.getId(contents.getStack().getItem()) : null;
				return new TextListTooltipComponent((contents.isPresent() ? Data.getText("relic_bundle_contents.contains", Data.getText(contentsId.getNamespace(), "relic." + contentsId.getPath())) : Data.getText("relic_bundle_contents.empty")).formatted(Formatting.GRAY));
			} else if (data instanceof RelicTooltipData(RelicComponent relic)) {
				return getRelic(Relic.data, relic);
			}
			return null;
		});
	}

	public static TextListTooltipComponent getRelic(RelicLoader relicLoader, RelicComponent relic) {
		boolean isValid = relic != null && relicLoader.get(relic.getId()).isPresent();
		Identifier relicId = isValid ? relic.id() : Data.idOf("none");
		List<MutableText> texts = new ArrayList<>();
		boolean isObfuscated = (ClientData.getMinecraft().player == null || !ClientData.getMinecraft().player.getInventory().contains(TagRegistry.Item.relicBundle)) && isValid;
		if (!isObfuscated) {
			texts.add(Text.empty());
			texts.add(Data.getText("relic", Data.getText("relic.description")).formatted(Formatting.GRAY));
			texts.add(Data.getText(relicId.getNamespace(), "relic." + relicId.getPath()).formatted(Formatting.BLUE));
		} else {
			texts.add(Data.getText("relic", Data.getText("relic.obfuscated")).formatted(Formatting.GRAY, Formatting.OBFUSCATED));
		}
		return new TextListTooltipComponent(texts.toArray(new MutableText[0]));
	}
}
