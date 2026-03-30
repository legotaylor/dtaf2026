package dev.dannytaylor.dtaf2026.common.registry.recipe;

import dev.dannytaylor.dtaf2026.common.data.Data;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class RecipeTypeRegistry {
	public static RecipeType<ExtractingRecipe> extracting;

	public static void bootstrap() {
	}

	public static <T extends Recipe<?>> RecipeType<T> register(String path) {
		return register(Data.idOf(path));
	}

	public static <T extends Recipe<?>> RecipeType<T> register(Identifier identifier) {
		return Registry.register(Registries.RECIPE_TYPE, identifier, new RecipeType<T>() {
			public String toString() {
				return identifier.toString();
			}
		});
	}

	static {
		extracting = register("extracting");
	}
}
