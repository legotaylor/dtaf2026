/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.dannytaylor.dtaf2026.client.registry.pipeline.SomniumRealeSettings;
import dev.dannytaylor.dtaf2026.common.data.Data;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gl.UniformType;
import net.minecraft.client.render.VertexFormats;

public class PipelineRegistry {
	public static final RenderPipeline tinyStars;
	public static final RenderPipeline stars;
	public static final RenderPipeline bigStars;
	public static final RenderPipeline sky;
	public static SomniumRealeSettings somniumRealeSettings;

	public static void bootstrap() {
		PostEffectRegistry.bootstrap();
	}

	private static RenderPipeline.Builder starsBuilder(String location) {
		String path = (location.isEmpty() ? "" : "_" + location);
		return RenderPipeline.builder(RenderPipelines.TRANSFORMS_AND_PROJECTION_SNIPPET).withLocation(Data.idOf("pipeline/stars" + path)).withVertexShader(Data.idOf("core/stars" + path)).withFragmentShader(Data.idOf("core/stars" + path)).withBlend(BlendFunction.OVERLAY).withDepthWrite(false).withVertexFormat(VertexFormats.POSITION, VertexFormat.DrawMode.QUADS).withUniform("SomniumReale", UniformType.UNIFORM_BUFFER);
	}

	private static RenderPipeline.Builder starsBuilder() {
		return starsBuilder("");
	}

	private static RenderPipeline.Builder skyBuilder() {
		return RenderPipeline.builder(RenderPipelines.TRANSFORMS_PROJECTION_FOG_SNIPPET).withLocation(Data.idOf("pipeline/sky")).withVertexShader(Data.idOf("core/sky")).withFragmentShader(Data.idOf("core/sky")).withDepthWrite(false).withVertexFormat(VertexFormats.POSITION, VertexFormat.DrawMode.TRIANGLE_FAN).withUniform("SomniumReale", UniformType.UNIFORM_BUFFER);
	}

	static {
		tinyStars = starsBuilder("tiny").build();
		stars = starsBuilder().build();
		bigStars = starsBuilder("big").build();
		sky = skyBuilder().build();
	}
}
