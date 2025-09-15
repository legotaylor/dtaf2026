/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry;

import dev.dannytaylor.dtaf2026.common.data.Data;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.biome.Biome;

public class TagRegistry {
	public static class Block {
		public static final TagKey<net.minecraft.block.Block> supportedLogs;
		static {
			supportedLogs = TagKey.of(RegistryKeys.BLOCK, Data.idOf("supported_logs"));
		}
	}
	public static class Enchantment {
		public static final TagKey<net.minecraft.enchantment.Enchantment> fullyBreaksSupportedLogs;
		static {
			fullyBreaksSupportedLogs = TagKey.of(RegistryKeys.ENCHANTMENT, Data.idOf("fully_breaks_supported_logs"));
		}
	}
	public static class Item {
		public static final TagKey<net.minecraft.item.Item> bark;
		public static final TagKey<net.minecraft.item.Item> supportedLogs;
		static {
			bark = TagKey.of(RegistryKeys.ITEM, Data.idOf("bark"));
			supportedLogs = TagKey.of(RegistryKeys.ITEM, Data.idOf("supported_logs"));
		}
	}
	public static class WorldGen {
		public static class Biome {
			public static final TagKey<net.minecraft.world.biome.Biome> somnium_reale;
			static {
				somnium_reale = TagKey.of(RegistryKeys.BIOME, Data.idOf("somnium_reale"));
			}
		}
	}
}
