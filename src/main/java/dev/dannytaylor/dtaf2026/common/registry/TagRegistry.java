/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry;

import dev.dannytaylor.dtaf2026.common.data.Data;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

public class TagRegistry {
	public static class Block {
		public static final TagKey<net.minecraft.block.Block> supportedWoodSetMaple;
		public static final TagKey<net.minecraft.block.Block> junglefowlEggHatchBooster;
		public static final TagKey<net.minecraft.block.Block> isCreakingHeartLog;
		public static final TagKey<net.minecraft.block.Block> alwaysSupportsSupportedBlocks;

		static {
			supportedWoodSetMaple = TagKey.of(RegistryKeys.BLOCK, Data.idOf("supported/wood_set/maple"));
			junglefowlEggHatchBooster = TagKey.of(RegistryKeys.BLOCK, Data.idOf("junglefowl_egg_hatch_booster"));
			isCreakingHeartLog = TagKey.of(RegistryKeys.BLOCK, Data.idOf("is_creaking_heart_log"));
			alwaysSupportsSupportedBlocks = TagKey.of(RegistryKeys.BLOCK, Data.idOf("always_supports_supported_blocks"));
		}
	}

	public static class Enchantment {
		public static final TagKey<net.minecraft.enchantment.Enchantment> fullyBreaksSupportedLogs;

		static {
			fullyBreaksSupportedLogs = TagKey.of(RegistryKeys.ENCHANTMENT, Data.idOf("fully_breaks_supported_logs"));
		}
	}

	public static class Item {
		public static final TagKey<net.minecraft.item.Item> alwaysRelicBundle;
		public static final TagKey<net.minecraft.item.Item> bark;
		public static final TagKey<net.minecraft.item.Item> boarFood;
		public static final TagKey<net.minecraft.item.Item> dayRelicBundle;
		public static final TagKey<net.minecraft.item.Item> junglefowlFood;
		public static final TagKey<net.minecraft.item.Item> mapleLogs;
		public static final TagKey<net.minecraft.item.Item> nightRelicBundle;
		public static final TagKey<net.minecraft.item.Item> relic;
		public static final TagKey<net.minecraft.item.Item> relicBundle;

		static {
			alwaysRelicBundle = TagKey.of(RegistryKeys.ITEM, Data.idOf("always_relic_bundle"));
			bark = TagKey.of(RegistryKeys.ITEM, Data.idOf("bark"));
			boarFood = TagKey.of(RegistryKeys.ITEM, Data.idOf("boar_food"));
			dayRelicBundle = TagKey.of(RegistryKeys.ITEM, Data.idOf("day_relic_bundle"));
			junglefowlFood = TagKey.of(RegistryKeys.ITEM, Data.idOf("junglefowl_food"));
			mapleLogs = TagKey.of(RegistryKeys.ITEM, Data.idOf("maple_logs"));
			nightRelicBundle = TagKey.of(RegistryKeys.ITEM, Data.idOf("night_relic_bundle"));
			relic = TagKey.of(RegistryKeys.ITEM, Data.idOf("relic"));
			relicBundle = TagKey.of(RegistryKeys.ITEM, Data.idOf("relic_bundle"));
		}
	}

	public static class WorldGen {
		public static class Biome {
			public static final TagKey<net.minecraft.world.biome.Biome> abstractSomniumReale;
			public static final TagKey<net.minecraft.world.biome.Biome> somniumReale;
			public static final TagKey<net.minecraft.world.biome.Biome> theTerrorlands;

			public static boolean isIn(WorldView world, BlockPos blockPos, TagKey<net.minecraft.world.biome.Biome> tag) {
				return world.getBiome(blockPos).isIn(tag);
			}

			static {
				abstractSomniumReale = TagKey.of(RegistryKeys.BIOME, Data.getSomniumRealeId().withPrefixedPath("is_abstract_"));
				somniumReale = TagKey.of(RegistryKeys.BIOME, Data.getSomniumRealeId().withPrefixedPath("is_"));
				theTerrorlands = TagKey.of(RegistryKeys.BIOME, Data.getSomniaMetusId().withPrefixedPath("is_"));
			}
		}
	}
}
