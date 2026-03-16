/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.iris;

import dev.dannytaylor.dtaf2026.client.registry.pipeline.PipelineRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.irisshaders.iris.api.v0.IrisApi;
import net.irisshaders.iris.gl.blending.AlphaTests;
import net.irisshaders.iris.gl.state.FogMode;
import net.irisshaders.iris.pipeline.IrisPipelines;
import net.irisshaders.iris.pipeline.programs.SomniumRealeShader;
import net.irisshaders.iris.shaderpack.loading.ProgramGroup;
import net.irisshaders.iris.shaderpack.loading.ProgramId;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.VertexFormats;

public class IrisMain {
	public static boolean isIrisInstalled() {
		return FabricLoader.getInstance().isModLoaded("iris");
	}
	public static boolean isIrisActive() {
		return isIrisInstalled() && IrisApi.getInstance().isShaderPackInUse();
	}
	public static void registerShaderKeys() {
		SomniumRealeShader.registerKey("dtaf2026_stars_tiny", new SomniumRealeShaderKey("dtaf2026_stars_tiny", AlphaTests.OFF, VertexFormats.POSITION, FogMode.OFF, SomniumRealeShader.LightingModel.FULLBRIGHT, PipelineRegistry.tinyStars));
		SomniumRealeShader.registerKey("dtaf2026_stars", new SomniumRealeShaderKey("dtaf2026_stars", AlphaTests.OFF, VertexFormats.POSITION, FogMode.OFF, SomniumRealeShader.LightingModel.FULLBRIGHT, PipelineRegistry.stars));
		SomniumRealeShader.registerKey("dtaf2026_stars_big", new SomniumRealeShaderKey("dtaf2026_stars_big", AlphaTests.OFF, VertexFormats.POSITION, FogMode.OFF, SomniumRealeShader.LightingModel.FULLBRIGHT, PipelineRegistry.bigStars));
		IrisPipelines.copyPipeline(RenderPipelines.POSITION_SKY, PipelineRegistry.sky);
		IrisPipelines.copyPipeline(RenderPipelines.CLOUDS, PipelineRegistry.clouds);
		IrisPipelines.copyPipeline(RenderPipelines.FLAT_CLOUDS, PipelineRegistry.flatClouds);
		IrisPipelines.copyPipeline(RenderPipelines.SOLID, PipelineRegistry.solid);
		IrisPipelines.copyPipeline(RenderPipelines.WIREFRAME, PipelineRegistry.wireframe);
		IrisPipelines.copyPipeline(RenderPipelines.CUTOUT, PipelineRegistry.cutout);
		IrisPipelines.copyPipeline(RenderPipelines.CUTOUT_MIPPED, PipelineRegistry.cutoutMipped);
		IrisPipelines.copyPipeline(RenderPipelines.TRANSLUCENT, PipelineRegistry.translucent);
	}

	public static void registerPrograms() {
		SomniumRealeShader.registerProgram("dtaf2026_stars_tiny", new SomniumRealeProgramId(ProgramGroup.Gbuffers, "dtaf2026_stars_tiny", ProgramId.SkyBasic, null));
		SomniumRealeShader.registerProgram("dtaf2026_stars", new SomniumRealeProgramId(ProgramGroup.Gbuffers, "dtaf2026_stars", ProgramId.SkyBasic, null));
		SomniumRealeShader.registerProgram("dtaf2026_stars_big", new SomniumRealeProgramId(ProgramGroup.Gbuffers, "dtaf2026_stars_big", ProgramId.SkyBasic, null));
	}
}
