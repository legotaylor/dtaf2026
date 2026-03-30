package dev.dannytaylor.dtaf2026.common.registry.recipe;

import dev.dannytaylor.dtaf2026.common.data.Data;
import net.minecraft.recipe.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class RecipeSerializerRegistry {
	public static RecipeSerializer<ExtractingRecipe> extracting;

	public static void bootstrap() {
	}

	public static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String path, S serializer) {
		return register(Data.idOf(path), serializer);
	}

	public static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(Identifier identifier, S serializer) {
		return Registry.register(Registries.RECIPE_SERIALIZER, identifier, serializer);
	}

	static {
		extracting = register("extracting", new AbstractCookingRecipe.Serializer<>(ExtractingRecipe::new, 100));
	}
}
