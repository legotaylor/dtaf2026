/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.item.tooltip;

import dev.dannytaylor.dtaf2026.common.registry.item.component.RelicBundleContentsComponent;
import net.minecraft.item.tooltip.TooltipData;

public record RelicBundleTooltipData(RelicBundleContentsComponent contents) implements TooltipData {
}
