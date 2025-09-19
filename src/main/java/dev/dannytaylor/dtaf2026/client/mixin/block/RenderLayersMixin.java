/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.mixin.block;

import dev.dannytaylor.dtaf2026.common.registry.block.SupportedLeavesBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.render.RenderLayers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderLayers.class)
public abstract class RenderLayersMixin {
	@Shadow private static boolean fancyGraphicsOrBetter;

	@Inject(method = "getBlockLayer", at = @At("RETURN"), cancellable = true)
	private static void dtaf2026$getBlockLayer(BlockState state, CallbackInfoReturnable<BlockRenderLayer> cir) {
		Block block = state.getBlock();
		if (block instanceof SupportedLeavesBlock) cir.setReturnValue(fancyGraphicsOrBetter ? BlockRenderLayer.CUTOUT_MIPPED : BlockRenderLayer.SOLID);
	}
}
