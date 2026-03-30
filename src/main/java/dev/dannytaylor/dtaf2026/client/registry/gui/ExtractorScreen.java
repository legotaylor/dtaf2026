package dev.dannytaylor.dtaf2026.client.registry.gui;

import dev.dannytaylor.dtaf2026.common.data.Data;
import dev.dannytaylor.dtaf2026.common.registry.gui.ExtractorScreenHandler;
import dev.dannytaylor.dtaf2026.common.registry.item.ItemRegistry;
import dev.dannytaylor.dtaf2026.common.registry.recipe.RecipeBookRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.AbstractFurnaceScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

@Environment(EnvType.CLIENT)
public class ExtractorScreen extends AbstractFurnaceScreen<ExtractorScreenHandler> {
	private static final Identifier LIT_PROGRESS_TEXTURE = Identifier.ofVanilla("container/blast_furnace/lit_progress");
	private static final Identifier BURN_PROGRESS_TEXTURE = Identifier.ofVanilla("container/blast_furnace/burn_progress");
	private static final Identifier TEXTURE = Identifier.ofVanilla("textures/gui/container/blast_furnace.png");
	private static final Text TOGGLE_BLASTABLE_TEXT = Data.getText("recipebook.toggleRecipes.extractable");
	private static final List<RecipeBookWidget.Tab> TABS;

	public ExtractorScreen(ExtractorScreenHandler container, PlayerInventory inventory, Text title) {
		super(container, inventory, title, TOGGLE_BLASTABLE_TEXT, TEXTURE, LIT_PROGRESS_TEXTURE, BURN_PROGRESS_TEXTURE, TABS);
	}

	static {
		TABS = List.of(new RecipeBookWidget.Tab(ItemRegistry.relicDust, RecipeBookRegistry.extracting));
	}
}
