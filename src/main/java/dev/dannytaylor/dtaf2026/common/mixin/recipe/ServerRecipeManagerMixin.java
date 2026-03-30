/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.mixin.recipe;

import dev.dannytaylor.dtaf2026.common.registry.recipe.RecipePropertySetRegistry;
import dev.dannytaylor.dtaf2026.common.registry.recipe.RecipeTypeRegistry;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipePropertySet;
import net.minecraft.recipe.ServerRecipeManager;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(ServerRecipeManager.class)
public abstract class ServerRecipeManagerMixin implements RecipeManager {
	@Inject(method = "initialize", at = @At("HEAD"))
	private static void dtaf2026$addExtractorRecipeSet(CallbackInfo ci) {
		if (ServerRecipeManager.SOLE_INGREDIENT_GETTERS != null) {
			Map<RegistryKey<RecipePropertySet>, ServerRecipeManager.SoleIngredientGetter> map = new HashMap<>(ServerRecipeManager.SOLE_INGREDIENT_GETTERS);
			map.put(RecipePropertySetRegistry.extractorInput, ServerRecipeManager.cookingIngredientGetter(RecipeTypeRegistry.extracting));
			ServerRecipeManager.SOLE_INGREDIENT_GETTERS = map;
		}
	}
}
