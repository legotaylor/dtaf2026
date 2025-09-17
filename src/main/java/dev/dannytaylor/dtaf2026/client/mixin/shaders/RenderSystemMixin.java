/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.mixin.shaders;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.dannytaylor.dtaf2026.client.registry.PipelineRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = RenderSystem.class, remap = false)
public abstract class RenderSystemMixin {
	@Inject(method = "bindDefaultUniforms", at = @At("RETURN"))
	private static void dtaf2026$bindDefaultUniforms(RenderPass pass, CallbackInfo ci) {
		GpuBuffer lightSettings = PipelineRegistry.somniumRealeSettings.get();
		if (lightSettings != null) pass.setUniform("SomniumReale", lightSettings);
	}
}
