package dev.dannytaylor.dtaf2026.common.registry.recipe;

import dev.dannytaylor.dtaf2026.common.data.Data;
import net.minecraft.recipe.RecipePropertySet;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class RecipePropertySetRegistry {
	public static final RegistryKey<RecipePropertySet> extractorInput;

	public static void bootstrap() {
	}

	public static RegistryKey<RecipePropertySet> register(String path) {
		return register(Data.idOf(path));
	}

	public static RegistryKey<RecipePropertySet> register(Identifier identifier) {
		return RegistryKey.of(RecipePropertySet.REGISTRY, identifier);
	}

	static {
		extractorInput = register("extractor_input");
	}
}
