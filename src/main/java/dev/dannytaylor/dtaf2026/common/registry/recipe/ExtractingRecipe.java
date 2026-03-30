package dev.dannytaylor.dtaf2026.common.registry.recipe;

import dev.dannytaylor.dtaf2026.common.registry.item.ItemRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.CookingRecipeCategory;
import net.minecraft.recipe.book.RecipeBookCategory;

public class ExtractingRecipe extends AbstractCookingRecipe {
	public ExtractingRecipe(String string, CookingRecipeCategory cookingRecipeCategory, Ingredient ingredient, ItemStack itemStack, float f, int i) {
		super(string, cookingRecipeCategory, ingredient, itemStack, f, i);
	}

	protected Item getCookerItem() {
		return ItemRegistry.extractor;
	}

	public RecipeSerializer<ExtractingRecipe> getSerializer() {
		return RecipeSerializerRegistry.extracting;
	}

	public RecipeType<ExtractingRecipe> getType() {
		return RecipeTypeRegistry.extracting;
	}

	public RecipeBookCategory getRecipeBookCategory() {
		return RecipeBookRegistry.extracting;
	}
}
