/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.mixin.item;

import dev.dannytaylor.dtaf2026.common.registry.item.ComponentTypeRegistry;
import dev.dannytaylor.dtaf2026.common.registry.item.tooltip.RelicTooltipData;
import dev.dannytaylor.dtaf2026.common.registry.relic.Relic;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(Item.class)
public abstract class ItemMixin {
	@Inject(method = "getTooltipData", at = @At("RETURN"), cancellable = true)
	public void dtaf2026$getTooltipData(ItemStack stack, CallbackInfoReturnable<Optional<TooltipData>> cir) {
		Identifier id = Registries.ITEM.getId((Item) (Object) this);
		if (Relic.data.get(id).isPresent()) {
			if (cir.getReturnValue().isEmpty()) {
				TooltipDisplayComponent tooltipDisplayComponent = stack.getOrDefault(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplayComponent.DEFAULT);
				cir.setReturnValue(!tooltipDisplayComponent.shouldDisplay(ComponentTypeRegistry.relic) ? Optional.empty() : Optional.ofNullable(stack.get(ComponentTypeRegistry.relic)).map(RelicTooltipData::new));
			}
		}
	}
}
