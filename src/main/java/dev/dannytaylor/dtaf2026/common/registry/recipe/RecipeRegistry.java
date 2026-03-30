package dev.dannytaylor.dtaf2026.common.registry.recipe;

public class RecipeRegistry {
	public static void bootstrap() {
		RecipeSerializerRegistry.bootstrap();
		RecipeTypeRegistry.bootstrap();
		RecipePropertySetRegistry.bootstrap();
		RecipeBookRegistry.bootstrap();
	}
}
