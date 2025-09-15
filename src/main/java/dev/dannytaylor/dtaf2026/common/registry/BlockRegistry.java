/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry;

import dev.dannytaylor.dtaf2026.common.data.Data;
import dev.dannytaylor.dtaf2026.common.registry.block.SupportedLogBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;

import java.util.function.Function;

public class BlockRegistry {
	public static final SupportedLogBlock maple_log;
	public static final SupportedLogBlock maple_wood;

	private static RegistryKey<Block> keyOf(String path) {
		return RegistryKey.of(RegistryKeys.BLOCK, Data.idOf(path));
	}

	private static Block register(String id, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings) {
		return Blocks.register(keyOf(id), factory, settings);
	}

	private static Block register(String id, AbstractBlock.Settings settings) {
		return register(id, Block::new, settings);
	}

	public static void bootstrap() {
	}

	public static AbstractBlock.Settings createLogSettings(MapColor topMapColor, MapColor sideMapColor, BlockSoundGroup sounds) {
		return Blocks.createLogSettings(topMapColor, sideMapColor, sounds).strength(3.0F);
	}

	static {
		maple_log = (SupportedLogBlock) register("maple_log", SupportedLogBlock::new, createLogSettings(MapColor.OAK_TAN, MapColor.SPRUCE_BROWN, BlockSoundGroup.WOOD));
		maple_wood = (SupportedLogBlock) register("maple_wood", SupportedLogBlock::new, createLogSettings(MapColor.OAK_TAN, MapColor.SPRUCE_BROWN, BlockSoundGroup.WOOD));
	}
}
