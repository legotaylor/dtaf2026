/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.iris;

import net.irisshaders.iris.gl.blending.BlendModeOverride;
import net.irisshaders.iris.shaderpack.loading.ProgramGroup;
import net.irisshaders.iris.shaderpack.loading.ProgramId;

public record SomniumRealeProgramId(ProgramGroup group, String sourceName, ProgramId fallback, BlendModeOverride defaultBlendOverride) {
}
