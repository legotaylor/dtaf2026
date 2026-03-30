/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.mixin.item;

import dev.dannytaylor.dtaf2026.common.registry.item.ComponentTypeRegistry;
import dev.dannytaylor.dtaf2026.common.registry.item.component.RelicComponent;
import dev.dannytaylor.dtaf2026.common.registry.relic.Relic;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.MergedComponentMap;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	@Mutable
	@Shadow
	@Final
	MergedComponentMap components;

	@Shadow
	public abstract Item getItem();

	@Inject(method = "<init>(Lnet/minecraft/item/ItemConvertible;ILnet/minecraft/component/MergedComponentMap;)V", at = @At("RETURN"))
	private void dtaf2026$init(ItemConvertible item, int count, MergedComponentMap components, CallbackInfo ci) {
		if (!this.components.contains(ComponentTypeRegistry.relic)) {
			Identifier id = Registries.ITEM.getId(this.getItem());
			if (Relic.data.get(id).isPresent()) {
				this.components = MergedComponentMap.create(this.components, ComponentChanges.builder().add(ComponentTypeRegistry.relic, new RelicComponent(id)).build());
			}
		}
	}
}
