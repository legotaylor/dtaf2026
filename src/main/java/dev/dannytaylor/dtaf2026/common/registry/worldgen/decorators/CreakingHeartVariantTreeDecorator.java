/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.worldgen.decorators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.dannytaylor.dtaf2026.common.registry.block.BlockRegistry;
import dev.dannytaylor.dtaf2026.common.registry.block.CreakingVariant;
import dev.dannytaylor.dtaf2026.common.registry.worldgen.DecoratorRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.block.CreakingHeartBlock;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.enums.CreakingHeartState;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CreakingHeartVariantTreeDecorator extends TreeDecorator {
	public static final MapCodec<CreakingHeartVariantTreeDecorator> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter(tree -> tree.probability),
		CreakingVariant.codec.fieldOf("variant").forGetter(tree -> tree.variant)
	).apply(instance, CreakingHeartVariantTreeDecorator::new));

	private final float probability;
	private final CreakingVariant variant;

	public CreakingHeartVariantTreeDecorator(float probability, CreakingVariant variant) {
		this.probability = probability;
		this.variant = variant;
	}

	protected TreeDecoratorType<?> getType() {
		return DecoratorRegistry.creakingHeartVariant;
	}

	public void generate(Generator generator) {
		Random random = generator.getRandom();
		List<BlockPos> list = generator.getLogPositions();
		if (!list.isEmpty()) {
			if (!(random.nextFloat() >= this.probability)) {
				List<BlockPos> list2 = new ArrayList<>(list);
				Util.shuffle(list2, random);
				Optional<BlockPos> optional = list2.stream().filter((pos) -> {
					Direction[] verticalDirections = new Direction[]{Direction.UP, Direction.DOWN};
					for (Direction direction : verticalDirections) {
						if (!generator.matches(pos.offset(direction), (state) -> state.isIn(BlockTags.LOGS) && state.get(PillarBlock.AXIS).isVertical()))
							return false;
					}
					return true;
				}).findFirst();
				optional.ifPresent(blockPos -> generator.replace(blockPos, Blocks.CREAKING_HEART.getDefaultState().with(BlockRegistry.Properties.creakingVariant, variant).with(CreakingHeartBlock.ACTIVE, CreakingHeartState.DORMANT).with(CreakingHeartBlock.NATURAL, true)));
			}
		}
	}
}
