/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.worldgen;

import dev.dannytaylor.dtaf2026.common.data.Data;
import net.minecraft.block.SaplingGenerator;

import java.util.Optional;

public class SaplingGeneratorRegistry {
	public static final SaplingGenerator maple = new SaplingGenerator(Data.idOf("maple").toString(), Optional.empty(), Optional.of(ConfiguredFeatureRegistry.mapleTree), Optional.empty());
	public static final SaplingGenerator cerulean = new SaplingGenerator(Data.idOf("cerulean").toString(), Optional.empty(), Optional.of(ConfiguredFeatureRegistry.ceruleanTree), Optional.empty());

	public static void bootstrap() {
	}
}
