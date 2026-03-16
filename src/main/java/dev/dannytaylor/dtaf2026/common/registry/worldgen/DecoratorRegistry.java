/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.worldgen;

import com.mojang.serialization.MapCodec;
import dev.dannytaylor.dtaf2026.common.data.Data;
import dev.dannytaylor.dtaf2026.common.registry.worldgen.decorators.CreakingHeartVariantTreeDecorator;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;

public class DecoratorRegistry {
	public static final TreeDecoratorType<CreakingHeartVariantTreeDecorator> creakingHeartVariant;

	static {
		creakingHeartVariant = registerTreeDecorator("creaking_heart", CreakingHeartVariantTreeDecorator.CODEC);
	}

	public static void bootstrap() {
	}

	public static <P extends TreeDecorator> TreeDecoratorType<P> registerTreeDecorator(String path, MapCodec<P> codec) {
		return registerTreeDecorator(Data.idOf(path), codec);
	}

	public static <P extends TreeDecorator> TreeDecoratorType<P> registerTreeDecorator(Identifier identifier, MapCodec<P> codec) {
		return Registry.register(Registries.TREE_DECORATOR_TYPE, identifier, new TreeDecoratorType<>(codec));
	}
}
