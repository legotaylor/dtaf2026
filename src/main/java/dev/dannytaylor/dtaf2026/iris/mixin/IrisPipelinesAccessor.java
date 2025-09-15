/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.iris.mixin;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import it.unimi.dsi.fastutil.Function;
import net.irisshaders.iris.pipeline.IrisPipelines;
import net.irisshaders.iris.pipeline.IrisRenderingPipeline;
import net.irisshaders.iris.pipeline.programs.ShaderKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = IrisPipelines.class, remap = false)
public interface IrisPipelinesAccessor {
	@Invoker("assignToMain")
	static void dtaf2026$invokeAssignToMain(RenderPipeline pipeline, Function<IrisRenderingPipeline, ShaderKey> o) {
		throw new UnsupportedOperationException();
	}
}
