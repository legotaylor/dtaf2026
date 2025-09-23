/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.mixin.item;

import dev.dannytaylor.dtaf2026.common.registry.item.ComponentTypeRegistry;
import dev.dannytaylor.dtaf2026.common.registry.item.component.RelicComponent;
import dev.dannytaylor.dtaf2026.common.registry.item.tooltip.RelicTooltipData;
import dev.dannytaylor.dtaf2026.common.registry.relic.RelicLoader;
import net.minecraft.component.ComponentMap;
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
	@Inject(method = "getComponents", at = @At("RETURN"), cancellable = true)
	private void dtaf2026$getDefaultStack(CallbackInfoReturnable<ComponentMap> cir) {
		Identifier id = Registries.ITEM.getId((Item) (Object) this);
		if (RelicLoader.get(id).isPresent()) {
			ComponentMap components = cir.getReturnValue();
			if (!components.contains(ComponentTypeRegistry.relic)) cir.setReturnValue(ComponentMap.of(components, ComponentMap.builder().add(ComponentTypeRegistry.relic, new RelicComponent(id)).build()));
		}
	}

	@Inject(method = "getTooltipData", at = @At("RETURN"), cancellable = true)
	public void getTooltipData(ItemStack stack, CallbackInfoReturnable<Optional<TooltipData>> cir) {
		Identifier id = Registries.ITEM.getId((Item) (Object) this);
		if (RelicLoader.get(id).isPresent()) {
			if (cir.getReturnValue().isEmpty()) {
				TooltipDisplayComponent tooltipDisplayComponent = stack.getOrDefault(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplayComponent.DEFAULT);
				cir.setReturnValue(!tooltipDisplayComponent.shouldDisplay(ComponentTypeRegistry.relic) ? Optional.empty() : Optional.ofNullable(stack.get(ComponentTypeRegistry.relic)).map(RelicTooltipData::new));
			}
		}
	}
}
