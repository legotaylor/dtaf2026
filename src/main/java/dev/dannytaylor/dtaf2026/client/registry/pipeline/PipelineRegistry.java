/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry.pipeline;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.PolygonMode;
import com.mojang.blaze3d.textures.TextureFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
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
	public static final RenderPipeline.Snippet terrain;
	public static final RenderPipeline solid;
	public static final RenderPipeline wireframe;
	public static final RenderPipeline cutoutMipped;
	public static final RenderPipeline cutout;
	public static final RenderPipeline translucent;
	public static final RenderPipeline tripwire;
	public static final RenderPipeline.Snippet titleSnippet;
	public static final RenderPipeline title;

	static {
		somniumReale = RenderPipeline.builder(RenderPipelines.TRANSFORMS_PROJECTION_FOG_SNIPPET).withUniform("SomniumReale", UniformType.UNIFORM_BUFFER).buildSnippet();
		tinyStars = register(starsBuilder("tiny").build());
		stars = register(starsBuilder().build());
		bigStars = register(starsBuilder("big").build());
		sky = register(skyBuilder().build());
		cloudsSnippet = builder().withVertexShader(Data.idOf("core/rendertype_clouds")).withFragmentShader(Data.idOf("core/rendertype_clouds")).withBlend(BlendFunction.TRANSLUCENT).withVertexFormat(VertexFormats.EMPTY, VertexFormat.DrawMode.QUADS).withUniform("CloudInfo",UniformType.UNIFORM_BUFFER).withUniform("CloudFaces",UniformType.TEXEL_BUFFER, TextureFormat.RED8I).buildSnippet();
		flatClouds = register(RenderPipeline.builder(cloudsSnippet).withLocation(Data.idOf("pipeline/flat_clouds")).withCull(false).build());
		clouds = register(RenderPipeline.builder(cloudsSnippet).withLocation(Data.idOf("pipeline/clouds")).build());

		terrain = RenderPipeline.builder(RenderPipelines.TRANSFORMS_PROJECTION_FOG_SNIPPET).withVertexShader(Data.idOf("core/terrain")).withFragmentShader(Data.idOf("core/terrain")).withSampler("Sampler0").withSampler("Sampler2").withVertexFormat(VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS).buildSnippet();
		solid = register(RenderPipeline.builder(terrain).withLocation("pipeline/solid").build());
		wireframe = register(RenderPipeline.builder(terrain).withLocation("pipeline/wireframe").withPolygonMode(PolygonMode.WIREFRAME).build());
		cutoutMipped = register(RenderPipeline.builder(terrain).withLocation("pipeline/cutout_mipped").withShaderDefine("ALPHA_CUTOUT", 0.5F).build());
		cutout = register(RenderPipeline.builder(terrain).withLocation("pipeline/cutout").withShaderDefine("ALPHA_CUTOUT", 0.1F).build());
		translucent = register(RenderPipeline.builder(terrain).withLocation("pipeline/translucent").withBlend(BlendFunction.TRANSLUCENT).build());
		tripwire = register(RenderPipeline.builder(terrain).withLocation("pipeline/tripwire").withShaderDefine("ALPHA_CUTOUT", 0.1F).withBlend(BlendFunction.TRANSLUCENT).build());
		titleSnippet = RenderPipeline.builder(RenderPipelines.TRANSFORMS_AND_PROJECTION_SNIPPET, RenderPipelines.FOG_SNIPPET, RenderPipelines.GLOBALS_SNIPPET).withVertexShader(Data.idOf("core/title")).withFragmentShader(Data.idOf("core/title")).withSampler("Sampler0").withSampler("Sampler1").withVertexFormat(VertexFormats.POSITION, VertexFormat.DrawMode.QUADS).buildSnippet();
		title = RenderPipeline.builder(titleSnippet).withLocation(Data.idOf("pipeline/title")).withShaderDefine("PORTAL_LAYERS", 15).build();
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

	public static void bootstrap() {
		// UBORegistry is registered through GameRendererMixin
		RenderLayerRegistry.bootstrap();
		PostEffectRegistry.bootstrap();
	}
}
