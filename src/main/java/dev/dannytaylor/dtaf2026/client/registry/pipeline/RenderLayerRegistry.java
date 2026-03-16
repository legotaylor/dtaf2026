/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry.pipeline;

import dev.dannytaylor.dtaf2026.common.data.Data;
import net.minecraft.client.render.RenderLayer;

public class RenderLayerRegistry {
	public static final RenderLayer solid;
	public static final RenderLayer cutoutMipped;
	public static final RenderLayer cutout;

	static {
		solid = RenderLayer.of(Data.idOf("solid").toString(), 1536, true, false, PipelineRegistry.solid, RenderLayer.MultiPhaseParameters.builder().lightmap(RenderLayer.ENABLE_LIGHTMAP).texture(RenderLayer.MIPMAP_BLOCK_ATLAS_TEXTURE).build(true));
		cutoutMipped = RenderLayer.of(Data.idOf("cutout_mipped").toString(), 1536, true, false, PipelineRegistry.cutoutMipped, RenderLayer.MultiPhaseParameters.builder().lightmap(RenderLayer.ENABLE_LIGHTMAP).texture(RenderLayer.MIPMAP_BLOCK_ATLAS_TEXTURE).build(true));
		cutout = RenderLayer.of(Data.idOf("cutout").toString(), 1536, true, false, PipelineRegistry.cutout, RenderLayer.MultiPhaseParameters.builder().lightmap(RenderLayer.ENABLE_LIGHTMAP).texture(RenderLayer.BLOCK_ATLAS_TEXTURE).build(true));
	}

	public static void bootstrap() {
	}
}
