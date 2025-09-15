/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.iris;

import dev.dannytaylor.dtaf2026.client.registry.PipelineRegistry;
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
		SomniumRealeShader.registerKey("DTAF2026TinyStars", new SomniumRealeShaderKey("DTAF2026TinyStars", AlphaTests.OFF, VertexFormats.POSITION, FogMode.OFF, SomniumRealeShader.LightingModel.FULLBRIGHT, PipelineRegistry.tinyStars));
		SomniumRealeShader.registerKey("DTAF2026Stars", new SomniumRealeShaderKey("DTAF2026Stars", AlphaTests.OFF, VertexFormats.POSITION, FogMode.OFF, SomniumRealeShader.LightingModel.FULLBRIGHT, PipelineRegistry.stars));
		SomniumRealeShader.registerKey("DTAF2026BigStars", new SomniumRealeShaderKey("DTAF2026BigStars", AlphaTests.OFF, VertexFormats.POSITION, FogMode.OFF, SomniumRealeShader.LightingModel.FULLBRIGHT, PipelineRegistry.bigStars));
		IrisPipelines.copyPipeline(RenderPipelines.POSITION_SKY, PipelineRegistry.sky);
	}

	public static void registerPrograms() {
		SomniumRealeShader.registerProgram("DTAF2026TinyStars", new SomniumRealeProgramId(ProgramGroup.Gbuffers, "dtaf2026_stars_tiny", ProgramId.SkyBasic, null));
		SomniumRealeShader.registerProgram("DTAF2026Stars", new SomniumRealeProgramId(ProgramGroup.Gbuffers, "dtaf2026_stars", ProgramId.SkyBasic, null));
		SomniumRealeShader.registerProgram("DTAF2026BigStars", new SomniumRealeProgramId(ProgramGroup.Gbuffers, "dtaf2026_stars_big", ProgramId.SkyBasic, null));
	}
}
