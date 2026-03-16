/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.mixin.shaders;

import dev.dannytaylor.dtaf2026.client.data.ClientData;
import dev.dannytaylor.dtaf2026.client.registry.dimension.ClientDimensionRegistry;
import dev.dannytaylor.dtaf2026.client.registry.pipeline.RenderLayerRegistry;
import net.minecraft.client.render.RenderLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderLayer.class)
public abstract class RenderLayerMixin {
	@Inject(at = @At("RETURN"), method = "getSolid", cancellable = true)
	private static void dtaf2026$getSolid(CallbackInfoReturnable<RenderLayer> cir) {
		if (ClientDimensionRegistry.isAbstractSomniumReale(ClientData.getMinecraft().world))
			cir.setReturnValue(RenderLayerRegistry.solid);
	}

	@Inject(at = @At("RETURN"), method = "getCutout", cancellable = true)
	private static void dtaf2026$getCutout(CallbackInfoReturnable<RenderLayer> cir) {
		if (ClientDimensionRegistry.isAbstractSomniumReale(ClientData.getMinecraft().world))
			cir.setReturnValue(RenderLayerRegistry.cutout);
	}

	@Inject(at = @At("RETURN"), method = "getCutoutMipped", cancellable = true)
	private static void dtaf2026$getCutoutMipped(CallbackInfoReturnable<RenderLayer> cir) {
		if (ClientDimensionRegistry.isAbstractSomniumReale(ClientData.getMinecraft().world))
			cir.setReturnValue(RenderLayerRegistry.cutoutMipped);
	}
}
