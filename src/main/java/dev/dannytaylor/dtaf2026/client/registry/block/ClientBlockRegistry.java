/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry.block;

import dev.dannytaylor.dtaf2026.common.registry.block.BlockRegistry;
import dev.dannytaylor.dtaf2026.common.registry.block.SupportedWoodBlockSet;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.render.BlockRenderLayer;

public class ClientBlockRegistry {
	public static void bootstrap() {
		BlockRenderLayer cutout = BlockRenderLayer.CUTOUT;
		for (SupportedWoodBlockSet woodBlockSet : BlockRegistry.woodBlockSets) {
			BlockRenderLayerMap.putBlock(woodBlockSet.sapling, cutout);
			BlockRenderLayerMap.putBlock(woodBlockSet.door, cutout);
			BlockRenderLayerMap.putBlock(woodBlockSet.trapdoor, cutout);
		}
		BlockRenderLayerMap.putBlock(BlockRegistry.violet, cutout);
		BlockRenderLayerMap.putBlock(BlockRegistry.pottedViolet, cutout);
		BlockRenderLayerMap.putBlock(BlockRegistry.aerostoneLantern, cutout);
		BlockRenderLayerMap.putBlock(BlockRegistry.terrorlandsPortal, BlockRenderLayer.TRANSLUCENT);
	}
}
