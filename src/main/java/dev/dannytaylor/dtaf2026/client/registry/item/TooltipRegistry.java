/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry.item;

import dev.dannytaylor.dtaf2026.client.data.ClientData;
import dev.dannytaylor.dtaf2026.common.data.Data;
import dev.dannytaylor.dtaf2026.common.registry.TagRegistry;
import dev.dannytaylor.dtaf2026.common.registry.item.component.RelicBundleContentsComponent;
import dev.dannytaylor.dtaf2026.common.registry.item.component.RelicComponent;
import dev.dannytaylor.dtaf2026.common.registry.item.tooltip.RelicBundleTooltipData;
import dev.dannytaylor.dtaf2026.common.registry.item.tooltip.RelicTooltipData;
import dev.dannytaylor.dtaf2026.common.registry.relic.Relic;
import dev.dannytaylor.dtaf2026.common.registry.relic.RelicLoader;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.minecraft.client.gui.tooltip.OrderedTextTooltipComponent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class TooltipRegistry {
	public static void bootstrap() {
		TooltipComponentCallback.EVENT.register(data -> {
			if (data instanceof RelicBundleTooltipData(RelicBundleContentsComponent contents)) {
				Text text = (contents.isPresent() ? ClientData.getText("relic_bundle_contents.contains", contents.getStack().getName()) : ClientData.getText("relic_bundle_contents.empty")).formatted(Formatting.GRAY);
				return new OrderedTextTooltipComponent(text.asOrderedText());
			} else if (data instanceof RelicTooltipData(RelicComponent relic)) {
				return getRelic(Relic.data, relic);
			}
			return null;
		});
	}

	public static OrderedTextTooltipComponent getRelic(RelicLoader relicLoader, RelicComponent relic) {
		boolean isValid = relic != null && relicLoader.get(relic.getId()).isPresent();
		Identifier relicId = isValid ? relic.id() : Data.idOf("none");
		return new OrderedTextTooltipComponent(ClientData.getText("relic", (((ClientData.getMinecraft().player == null || !ClientData.getMinecraft().player.getInventory().contains(TagRegistry.Item.relicBundle)) && isValid) ? ClientData.getText("relic.obfuscated").formatted(Formatting.OBFUSCATED) : ClientData.getText(relicId.getNamespace(), "relic." + relicId.getPath()))).formatted(Formatting.GRAY, Formatting.ITALIC).asOrderedText());
	}
}
