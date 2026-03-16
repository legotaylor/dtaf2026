/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.dannytaylor.dtaf2026.common.registry.BlockRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.RandomPatchFeatureConfig;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;

import java.util.List;
import java.util.Optional;

public class GrassBlock extends AlwaysSpreadableBlock implements Fertilizable {
	public static final MapCodec<GrassBlock> codec = RecordCodecBuilder.mapCodec(
		instance -> instance.group(
				BlockState.CODEC.fieldOf("short_grass_state").forGetter(block -> block.shortGrassState),
				createSettingsCodec()
			)
			.apply(instance, GrassBlock::new)
	);
	private final BlockState shortGrassState;

	public GrassBlock(BlockState shortGrassState, AbstractBlock.Settings settings) {
		super(settings, BlockRegistry.dirt);
		this.shortGrassState = shortGrassState;
	}

	public MapCodec<? extends GrassBlock> getCodec() {
		return codec;
	}

	public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
		return world.getBlockState(pos.up()).isAir();
	}

	public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
		return true;
	}

	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
		BlockPos blockPos = pos.up();
		Optional<RegistryEntry.Reference<PlacedFeature>> optional = world.getRegistryManager().getOrThrow(RegistryKeys.PLACED_FEATURE).getOptional(VegetationPlacedFeatures.GRASS_BONEMEAL);
		label51:
		for (int i = 0; i < 128; ++i) {
			BlockPos blockPos2 = blockPos;

			for (int j = 0; j < i / 16; ++j) {
				blockPos2 = blockPos2.add(random.nextInt(3) - 1, (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1);
				if (!world.getBlockState(blockPos2.down()).isOf(this) || world.getBlockState(blockPos2).isFullCube(world, blockPos2))
					continue label51;
			}

			BlockState blockState2 = world.getBlockState(blockPos2);
			if (blockState2.isOf(this.shortGrassState.getBlock()) && random.nextInt(10) == 0) {
				if (this.shortGrassState.getBlock() instanceof Fertilizable fertilizable && fertilizable.isFertilizable(world, blockPos2, blockState2)) {
					fertilizable.grow(world, random, blockPos2, blockState2);
				}
			}

			if (blockState2.isAir()) {
				RegistryEntry<PlacedFeature> registryEntry;
				if (random.nextInt(8) == 0) {
					List<ConfiguredFeature<?, ?>> list = world.getBiome(blockPos2).value().getGenerationSettings().getFlowerFeatures();
					if (list.isEmpty()) continue;
					int k = random.nextInt(list.size());
					registryEntry = ((RandomPatchFeatureConfig) list.get(k).config()).feature();
				} else {
					if (optional.isEmpty()) continue;
					registryEntry = optional.get();
				}
				registryEntry.value().generateUnregistered(world, world.getChunkManager().getChunkGenerator(), random, blockPos2);
			}
		}

	}

	public Fertilizable.FertilizableType getFertilizableType() {
		return FertilizableType.NEIGHBOR_SPREADER;
	}
}
