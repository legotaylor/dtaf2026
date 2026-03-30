package dev.dannytaylor.dtaf2026.common.registry.recipe;

import dev.dannytaylor.dtaf2026.common.data.Data;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class RecipeBookRegistry {
	public static final RecipeBookCategory extracting;

	public static void bootstrap() {
	}

	public static RecipeBookCategory register(String path) {
		return register(Data.idOf(path));
	}

	public static RecipeBookCategory register(Identifier identifier) {
		return Registry.register(Registries.RECIPE_BOOK_CATEGORY, identifier, new RecipeBookCategory());
	}

	static {
		extracting = register("extracting");
	}
}
