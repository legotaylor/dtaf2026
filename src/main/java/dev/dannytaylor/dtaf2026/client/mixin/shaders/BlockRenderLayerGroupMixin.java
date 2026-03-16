/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.mixin.shaders;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import dev.dannytaylor.dtaf2026.client.data.ClientData;
import dev.dannytaylor.dtaf2026.client.registry.ClientDimensionRegistry;
import dev.dannytaylor.dtaf2026.client.registry.PipelineRegistry;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.BlockRenderLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockRenderLayer.class)
public abstract class BlockRenderLayerGroupMixin {
	@Inject(at = @At(value = "RETURN"), method = "getPipeline", cancellable = true)
	private void dtaf2026$getPipeline(CallbackInfoReturnable<RenderPipeline> cir) {
		if (ClientDimensionRegistry.isAbstractSomniumReale(ClientData.getMinecraft().world)) {
			RenderPipeline pipeline = cir.getReturnValue();
			if (pipeline == RenderPipelines.SOLID) cir.setReturnValue(PipelineRegistry.solid);
			else if (pipeline == RenderPipelines.CUTOUT) cir.setReturnValue(PipelineRegistry.cutout);
			else if (pipeline == RenderPipelines.CUTOUT_MIPPED) cir.setReturnValue(PipelineRegistry.cutoutMipped);
			else if (pipeline == RenderPipelines.TRANSLUCENT) cir.setReturnValue(PipelineRegistry.translucent);
			else if (pipeline == RenderPipelines.WIREFRAME) cir.setReturnValue(PipelineRegistry.wireframe);
		}
	}
}
