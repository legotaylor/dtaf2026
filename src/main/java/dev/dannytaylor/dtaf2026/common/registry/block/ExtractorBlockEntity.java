package dev.dannytaylor.dtaf2026.common.registry.block;

import dev.dannytaylor.dtaf2026.common.data.Data;
import dev.dannytaylor.dtaf2026.common.registry.gui.ExtractorScreenHandler;
import dev.dannytaylor.dtaf2026.common.registry.recipe.RecipeTypeRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.FuelRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class ExtractorBlockEntity extends AbstractFurnaceBlockEntity {
	public ExtractorBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntityRegistry.extractor, pos, state, RecipeTypeRegistry.extracting);
	}

	protected Text getContainerName() {
		return Data.getText("container.extractor");
	}

	protected int getFuelTime(FuelRegistry fuelRegistry, ItemStack stack) {
		return super.getFuelTime(fuelRegistry, stack) / 2;
	}

	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new ExtractorScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
	}
}
