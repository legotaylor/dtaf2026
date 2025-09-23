/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry;

import dev.dannytaylor.dtaf2026.client.data.ClientData;
import dev.dannytaylor.dtaf2026.common.data.Data;
import dev.dannytaylor.dtaf2026.common.registry.TagRegistry;
import dev.dannytaylor.dtaf2026.common.registry.item.component.ArcaNocturnaContentsComponent;
import dev.dannytaylor.dtaf2026.common.registry.item.component.RelicComponent;
import dev.dannytaylor.dtaf2026.common.registry.item.tooltip.ArcaNocturnaTooltipData;
import dev.dannytaylor.dtaf2026.common.registry.item.tooltip.RelicTooltipData;
import dev.dannytaylor.dtaf2026.common.registry.relic.RelicLoader;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.minecraft.client.gui.tooltip.OrderedTextTooltipComponent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class TooltipRegistry {
	public static void bootstrap() {
		TooltipComponentCallback.EVENT.register(data -> {
			if (data instanceof ArcaNocturnaTooltipData(ArcaNocturnaContentsComponent contents)) {
				Text text = (contents.isPresent() ? ClientData.getText("arca_nocturna_contents.contains", contents.getStack().getName()) : ClientData.getText("arca_nocturna_contents.empty")).formatted(Formatting.GRAY);
				return new OrderedTextTooltipComponent(text.asOrderedText());
			} else if (data instanceof RelicTooltipData(RelicComponent relic)) {
				boolean isValid = relic != null && RelicLoader.get(relic.getId()).isPresent();
				Identifier relicId = isValid ? relic.id() : Data.idOf("none");
				return new OrderedTextTooltipComponent(ClientData.getText("relic", (((ClientData.getMinecraft().player == null || !ClientData.getMinecraft().player.getInventory().contains(TagRegistry.Item.arcaNocturna)) && isValid) ? ClientData.getText("relic.obfuscated").formatted(Formatting.OBFUSCATED) : ClientData.getText(relicId.getNamespace(), "relic." + relicId.getPath()))).formatted(Formatting.GRAY, Formatting.ITALIC).asOrderedText());
			}
			return null;
		});
	}
}
