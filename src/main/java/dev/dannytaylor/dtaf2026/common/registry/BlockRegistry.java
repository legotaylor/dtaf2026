/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry;

import dev.dannytaylor.dtaf2026.common.data.Data;
import dev.dannytaylor.dtaf2026.common.registry.block.SupportedWoodBlockSet;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class BlockRegistry {
	public static final List<SupportedWoodBlockSet> woodBlockSets;
	public static final SupportedWoodBlockSet maple;

	public static RegistryKey<Block> keyOf(Identifier path) {
		return RegistryKey.of(RegistryKeys.BLOCK, path);
	}

	public static Block register(Identifier id, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings) {
		return Blocks.register(keyOf(id), factory, settings);
	}

	public static Block register(String id, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings) {
		return register(Data.idOf(id), factory, settings);
	}

	public static Block register(Identifier id, AbstractBlock.Settings settings) {
		return register(id, Block::new, settings);
	}

	public static Block register(String id, AbstractBlock.Settings settings) {
		return register(Data.idOf(id), settings);
	}

	public static void bootstrap() {
	}

	public static SupportedWoodBlockSet register(SupportedWoodBlockSet.Builder builder) {
		SupportedWoodBlockSet blockSet = builder.build();
		woodBlockSets.add(blockSet);
		return blockSet;
	}

	public static String getLogIdPrefix(int variant, boolean creative) {
		String id = getIdPrefix(creative);
		if (variant == 1 || variant == 2) {
			if (variant == 2) id += "twice_";
			id += "stripped_";
		}
		return id;
	}

	public static String getIdPrefix(boolean creative) {
		return creative ? "creative_" : "";
	}

	static {
		woodBlockSets = new ArrayList<>();
		maple = register(SupportedWoodBlockSet.builder("maple"));
	}
}
