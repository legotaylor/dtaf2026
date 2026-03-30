package dev.dannytaylor.dtaf2026.common.registry.gui;

import dev.dannytaylor.dtaf2026.common.registry.recipe.RecipePropertySetRegistry;
import dev.dannytaylor.dtaf2026.common.registry.recipe.RecipeTypeRegistry;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.book.RecipeBookType;
import net.minecraft.screen.AbstractFurnaceScreenHandler;
import net.minecraft.screen.PropertyDelegate;

public class ExtractorScreenHandler extends AbstractFurnaceScreenHandler {
	public ExtractorScreenHandler(int syncId, PlayerInventory playerInventory) {
		super(ScreenHandlerRegistry.extractor, RecipeTypeRegistry.extracting, RecipePropertySetRegistry.extractorInput, RecipeBookType.BLAST_FURNACE, syncId, playerInventory);
	}

	public ExtractorScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
		super(ScreenHandlerRegistry.extractor, RecipeTypeRegistry.extracting, RecipePropertySetRegistry.extractorInput, RecipeBookType.BLAST_FURNACE, syncId, playerInventory, inventory, propertyDelegate);
	}
}
