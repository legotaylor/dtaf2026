/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.textures.TextureFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.dannytaylor.dtaf2026.client.registry.pipeline.PostEffectRegistry;
import dev.dannytaylor.dtaf2026.common.data.Data;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gl.UniformType;
import net.minecraft.client.render.VertexFormats;

import java.util.ArrayList;
import java.util.List;

public class PipelineRegistry {
	public static final RenderPipeline.Snippet somniumReale;
	public static final RenderPipeline tinyStars;
	public static final RenderPipeline stars;
	public static final RenderPipeline bigStars;
	public static final RenderPipeline sky;
	public static final RenderPipeline.Snippet cloudsSnippet;
	public static final RenderPipeline flatClouds;
	public static final RenderPipeline clouds;

	public static void bootstrap() {
		// UBORegistry is registered through GameRendererMixin
		PostEffectRegistry.bootstrap();
	}

	private static RenderPipeline.Builder starsBuilder(String location) {
		String path = (location.isEmpty() ? "" : "_" + location);
		return builder().withLocation(Data.idOf("pipeline/stars" + path)).withVertexShader(Data.idOf("core/stars" + path)).withFragmentShader(Data.idOf("core/stars" + path)).withBlend(BlendFunction.OVERLAY).withDepthWrite(false).withVertexFormat(VertexFormats.POSITION, VertexFormat.DrawMode.QUADS);
	}

	private static RenderPipeline.Builder starsBuilder() {
		return starsBuilder("");
	}

	private static RenderPipeline.Builder skyBuilder() {
		return builder().withLocation(Data.idOf("pipeline/sky")).withVertexShader(Data.idOf("core/sky")).withFragmentShader(Data.idOf("core/sky")).withDepthWrite(false).withVertexFormat(VertexFormats.POSITION, VertexFormat.DrawMode.TRIANGLE_FAN);
	}

	public static RenderPipeline register(RenderPipeline renderPipeline) {
		return RenderPipelines.register(renderPipeline);
	}

	public static RenderPipeline.Builder builder(RenderPipeline.Snippet... snippets) {
		List<RenderPipeline.Snippet> snippetList = new ArrayList<>();
		snippetList.add(somniumReale);
		snippetList.addAll(List.of(snippets));
		return RenderPipeline.builder(snippetList.toArray(new RenderPipeline.Snippet[0]));
	}

	static {
		somniumReale = RenderPipeline.builder(RenderPipelines.TRANSFORMS_PROJECTION_FOG_SNIPPET).withUniform("SomniumReale", UniformType.UNIFORM_BUFFER).buildSnippet();
		tinyStars = register(starsBuilder("tiny").build());
		stars = register(starsBuilder().build());
		bigStars = register(starsBuilder("big").build());
		sky = register(skyBuilder().build());
		cloudsSnippet = builder().withVertexShader(Data.idOf("core/rendertype_clouds")).withFragmentShader(Data.idOf("core/rendertype_clouds")).withBlend(BlendFunction.TRANSLUCENT).withVertexFormat(VertexFormats.EMPTY, VertexFormat.DrawMode.QUADS).withUniform("CloudInfo",UniformType.UNIFORM_BUFFER).withUniform("CloudFaces",UniformType.TEXEL_BUFFER, TextureFormat.RED8I).buildSnippet();
		flatClouds = register(RenderPipeline.builder(cloudsSnippet).withLocation(Data.idOf("pipeline/flat_clouds")).withCull(false).build());
		clouds = register(RenderPipeline.builder(cloudsSnippet).withLocation(Data.idOf("pipeline/clouds")).build());
	}
}
