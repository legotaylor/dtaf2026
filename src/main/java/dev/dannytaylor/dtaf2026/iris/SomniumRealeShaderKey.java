/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.iris;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.irisshaders.iris.gl.blending.AlphaTest;
import net.irisshaders.iris.gl.state.FogMode;
import net.irisshaders.iris.pipeline.programs.SomniumRealeShader;

public record SomniumRealeShaderKey(String program, AlphaTest alphaTest, VertexFormat vertexFormat, FogMode fogMode, SomniumRealeShader.LightingModel lightingModel, RenderPipeline pipeline) {
}
