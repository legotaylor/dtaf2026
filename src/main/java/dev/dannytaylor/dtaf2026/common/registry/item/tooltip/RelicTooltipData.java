/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.item.tooltip;

import dev.dannytaylor.dtaf2026.common.registry.item.component.RelicComponent;
import net.minecraft.item.tooltip.TooltipData;

public record RelicTooltipData(RelicComponent relic) implements TooltipData {
}
