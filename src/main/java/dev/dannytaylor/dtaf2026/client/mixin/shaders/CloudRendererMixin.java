/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.mixin.shaders;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import dev.dannytaylor.dtaf2026.client.data.ClientData;
import dev.dannytaylor.dtaf2026.client.registry.ClientDimensionRegistry;
import dev.dannytaylor.dtaf2026.client.registry.PipelineRegistry;
import net.minecraft.client.option.CloudRenderMode;
import net.minecraft.client.render.CloudRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(CloudRenderer.class)
public abstract class CloudRendererMixin {
	@ModifyArg(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderPass;setPipeline(Lcom/mojang/blaze3d/pipeline/RenderPipeline;)V"), method = "renderClouds")
	private RenderPipeline dtaf2025$renderClouds(RenderPipeline pipeline) {
		return ClientDimensionRegistry.isSomniumReale(ClientData.getMinecraft().world) ? (ClientData.getMinecraft().options.getCloudRenderModeValue() == CloudRenderMode.FANCY ? PipelineRegistry.clouds : PipelineRegistry.flatClouds) : pipeline;
	}
}
