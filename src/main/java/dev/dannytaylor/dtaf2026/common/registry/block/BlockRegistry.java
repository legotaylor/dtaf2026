/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.block;

import dev.dannytaylor.dtaf2026.common.data.Data;
import dev.dannytaylor.dtaf2026.common.registry.entity.junglefowl.JunglefowlVariant;
import dev.dannytaylor.dtaf2026.common.registry.particle.ParticleRegistry;
import dev.dannytaylor.dtaf2026.common.registry.worldgen.SaplingGeneratorRegistry;
import net.minecraft.block.*;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class BlockRegistry {
	public static final List<SupportedWoodBlockSet> woodBlockSets;

	public static final Block grassBlock;
	public static final Block dryGrassBlock;
	public static final Block dirt;

	public static final Block stone;
	public static final Block amberOre;
	public static final Block jasperOre;
	public static final Block stoneStairs;
	public static final Block stoneSlab;

	public static final Block cobblestone;
	public static final Block cobblestoneStairs;
	public static final Block cobblestoneSlab;
	public static final Block cobblestoneWall;

	public static final Block deepslate;
	public static final Block deepslateAmberOre;
	public static final Block deepslateJasperOre;
	public static final Block deepslateStairs;
	public static final Block deepslateSlab;

	public static final Block cobbledDeepslate;
	public static final Block cobbledDeepslateStairs;
	public static final Block cobbledDeepslateSlab;
	public static final Block cobbledDeepslateWall;

	public static final SupportedWoodBlockSet maple;
	public static final SupportedWoodBlockSet cerulean;

	public static final FlowerBlock violet;
	public static final FlowerPotBlock pottedViolet;

	public static final JunglefowlEggBlock redEgg;
	public static final JunglefowlEggBlock largeRedEgg;
	public static final JunglefowlEggBlock grayEgg;
	public static final JunglefowlEggBlock largeGrayEgg;

	public static final Block terrorlandsPortal;

	public static RegistryKey<Block> keyOf(Identifier path) {
		return RegistryKey.of(RegistryKeys.BLOCK, path);
	}

	public static <T extends Block> T register(RegistryKey<Block> key, Function<AbstractBlock.Settings, T> factory, AbstractBlock.Settings settings) {
		T block = factory.apply(settings.registryKey(key));
		return Registry.register(Registries.BLOCK, key, block);
	}

	public static <T extends Block> T register(Identifier id, Function<AbstractBlock.Settings, T> factory, AbstractBlock.Settings settings) {
		return register(keyOf(id), factory, settings);
	}

	public static <T extends Block> T register(String id, Function<AbstractBlock.Settings, T> factory, AbstractBlock.Settings settings) {
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
		return register(builder, 0.01F);
	}

	public static SupportedWoodBlockSet register(SupportedWoodBlockSet.Builder builder, float leafParticleChance) {
		SupportedWoodBlockSet blockSet = builder.build(leafParticleChance);
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

		grassBlock = register("grass_block", (settings) -> new GrassBlock(Blocks.SHORT_GRASS.getDefaultState(), settings), AbstractBlock.Settings.copy(Blocks.GRASS_BLOCK));
		dryGrassBlock = register("dry_grass_block", (settings) -> new GrassBlock(Blocks.SHORT_DRY_GRASS.getDefaultState(), settings), AbstractBlock.Settings.copy(grassBlock));
		dirt = register("dirt", Block::new, AbstractBlock.Settings.copy(Blocks.DIRT));

		stone = register("stone", Block::new, AbstractBlock.Settings.copy(Blocks.STONE));
		amberOre = register("amber_ore", (settings) -> new ExperienceDroppingBlock(UniformIntProvider.create(3, 7), settings), AbstractBlock.Settings.create().mapColor(stone.getDefaultMapColor()).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(3.0F, 3.0F));
		jasperOre = register("jasper_ore", (settings) -> new ExperienceDroppingBlock(UniformIntProvider.create(3, 7), settings), AbstractBlock.Settings.create().mapColor(stone.getDefaultMapColor()).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(3.0F, 3.0F));
		stoneStairs = register("stone_stairs", (settings) -> new StairsBlock(stone.getDefaultState(), settings), AbstractBlock.Settings.copyShallow(stone));
		stoneSlab = register("stone_slab", SlabBlock::new, AbstractBlock.Settings.create().mapColor(stone.getDefaultMapColor()).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(2.0F, 6.0F));

		cobblestone = register("cobblestone", Block::new, AbstractBlock.Settings.copy(Blocks.COBBLESTONE));
		cobblestoneStairs = register("cobblestone_stairs", (settings) -> new StairsBlock(cobblestone.getDefaultState(), settings), AbstractBlock.Settings.copyShallow(cobblestone));
		cobblestoneSlab = register("cobblestone_slab", SlabBlock::new, AbstractBlock.Settings.create().mapColor(cobblestone.getDefaultMapColor()).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(2.0F, 6.0F));
		cobblestoneWall = register("cobblestone_wall", WallBlock::new, AbstractBlock.Settings.copyShallow(cobblestone).solid());

		deepslate = register("deepslate", Block::new, AbstractBlock.Settings.copy(Blocks.DEEPSLATE));
		deepslateAmberOre = register("deepslate_amber_ore", (settings) -> new ExperienceDroppingBlock(UniformIntProvider.create(3, 7), settings), AbstractBlock.Settings.copyShallow(amberOre).mapColor(deepslate.getDefaultMapColor()).strength(4.5F, 3.0F).sounds(BlockSoundGroup.DEEPSLATE));
		deepslateJasperOre = register("deepslate_jasper_ore", (settings) -> new ExperienceDroppingBlock(UniformIntProvider.create(3, 7), settings), AbstractBlock.Settings.copyShallow(jasperOre).mapColor(deepslate.getDefaultMapColor()).strength(4.5F, 3.0F).sounds(BlockSoundGroup.DEEPSLATE));
		deepslateStairs = register("deepslate_stairs", (settings) -> new StairsBlock(deepslate.getDefaultState(), settings), AbstractBlock.Settings.copyShallow(stone));
		deepslateSlab = register("deepslate_slab", SlabBlock::new, AbstractBlock.Settings.create().mapColor(deepslate.getDefaultMapColor()).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(2.0F, 6.0F));

		cobbledDeepslate = register("cobbled_deepslate", Block::new, AbstractBlock.Settings.copy(Blocks.COBBLED_DEEPSLATE));
		cobbledDeepslateStairs = register("cobbled_deepslate_stairs", (settings) -> new StairsBlock(cobblestone.getDefaultState(), settings), AbstractBlock.Settings.copyShallow(cobblestone));
		cobbledDeepslateSlab = register("cobbled_deepslate_slab", SlabBlock::new, AbstractBlock.Settings.create().mapColor(cobblestone.getDefaultMapColor()).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(2.0F, 6.0F));
		cobbledDeepslateWall = register("cobbled_deepslate_wall", WallBlock::new, AbstractBlock.Settings.copyShallow(cobbledDeepslate).solid());

		maple = register(SupportedWoodBlockSet.builder("maple"), 0.02F);
		cerulean = register(SupportedWoodBlockSet.builder("cerulean").saplingGenerator(SaplingGeneratorRegistry.cerulean).leaveParticles(ParticleRegistry.ceruleanLeaves), 0.02F);

		violet = register("violet", (settings) -> new FlowerBlock(StatusEffects.REGENERATION, 12.0F, settings), AbstractBlock.Settings.create().mapColor(MapColor.DARK_GREEN).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS).offset(AbstractBlock.OffsetType.XZ).pistonBehavior(PistonBehavior.DESTROY));
		pottedViolet = register("potted_violet", (settings) -> new FlowerPotBlock(violet, settings), Blocks.createFlowerPotSettings());

		redEgg = register("red_egg", (settings) -> JunglefowlEggBlock.of(settings, JunglefowlVariant.Model.small, Data.idOf("red")), AbstractBlock.Settings.create());
		largeRedEgg = register("large_red_egg", (settings) -> JunglefowlEggBlock.of(settings, JunglefowlVariant.Model.big, Data.idOf("large_red")), AbstractBlock.Settings.create());
		grayEgg = register("gray_egg", (settings) -> JunglefowlEggBlock.of(settings, JunglefowlVariant.Model.small, Data.idOf("gray")), AbstractBlock.Settings.create());
		largeGrayEgg = register("large_gray_egg", (settings) -> JunglefowlEggBlock.of(settings, JunglefowlVariant.Model.big, Data.idOf("gray")), AbstractBlock.Settings.create());

		terrorlandsPortal = register("terrorlands_portal", TerrorlandsPortalBlock::new, AbstractBlock.Settings.create().noCollision().ticksRandomly().strength(-1.0F).sounds(BlockSoundGroup.GLASS).luminance((state) -> 11).pistonBehavior(PistonBehavior.BLOCK));
	}

	public static class Properties {
		public static final EnumProperty<CreakingVariant> creakingVariant;

		static {
			creakingVariant = EnumProperty.of(Data.idOf("creaking_variant").toUnderscoreSeparatedString(), CreakingVariant.class);
		}
	}
}
