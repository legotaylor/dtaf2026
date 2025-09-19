/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry;

import dev.dannytaylor.dtaf2026.common.registry.BlockRegistry;
import dev.dannytaylor.dtaf2026.common.registry.block.SupportedWoodBlockSet;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.render.BlockRenderLayer;

public class ClientBlockRegistry {
	public static void bootstrap() {
		BlockRenderLayer saplingRenderLayer = BlockRenderLayer.CUTOUT;
		for (SupportedWoodBlockSet woodBlockSet : BlockRegistry.woodBlockSets) BlockRenderLayerMap.putBlock(woodBlockSet.sapling, saplingRenderLayer);
	}
}
