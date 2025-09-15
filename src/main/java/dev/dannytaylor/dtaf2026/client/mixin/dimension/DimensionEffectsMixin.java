/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.mixin.dimension;

import dev.dannytaylor.dtaf2026.client.registry.ClientDimensionRegistry;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DimensionEffects.class)
public abstract class DimensionEffectsMixin {
	@Inject(method = "byDimensionType", at = @At("HEAD"), cancellable = true)
	private static void dtaf2025$byDimensionType(DimensionType dimensionType, CallbackInfoReturnable<DimensionEffects> cir) {
		DimensionEffects effects = ClientDimensionRegistry.getEffectType(dimensionType.effects());
		if (effects != null) cir.setReturnValue(effects);
	}
}
