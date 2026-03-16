/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.item;

import dev.dannytaylor.dtaf2026.common.registry.TagRegistry;
import dev.dannytaylor.dtaf2026.common.registry.item.component.RelicBundleContentsComponent;
import dev.dannytaylor.dtaf2026.common.registry.item.component.RelicComponent;
import dev.dannytaylor.dtaf2026.common.registry.item.tooltip.RelicBundleTooltipData;
import dev.dannytaylor.dtaf2026.common.registry.relic.Relic;
import dev.dannytaylor.dtaf2026.common.registry.relic.RelicLoader;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ClickType;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.stream.Stream;

public class RelicBundleItem extends Item {
	public RelicBundleItem(Item.Settings settings) {
		super(settings);
	}

	public static RelicBundleContentsComponent getContents(ItemStack relicBundle) {
		return relicBundle.get(ComponentTypeRegistry.relicBundleContents);
	}

	public void inventoryTick(ItemStack relicBundle, ServerWorld world, Entity entity, @Nullable EquipmentSlot slot) {
		super.inventoryTick(relicBundle, world, entity, slot);
		RelicLoader loader = getLoader();
		if (loader != null) {
			if (entity instanceof LivingEntity livingEntity) {
				RelicBundleContentsComponent contents = getContents(relicBundle);
				if (contents.isPresent()) {
					RelicComponent relicComponent = contents.getStack().get(ComponentTypeRegistry.relic);
					if (relicComponent != null) loader.get(relicComponent.getId()).ifPresent((relic) -> relic.tick(livingEntity, relicBundle));
				}
			}
		}
	}

	public boolean onStackClicked(ItemStack relicBundle, Slot slot, ClickType clickType, PlayerEntity player) {
		RelicBundleContentsComponent contents = getContents(relicBundle);
		if (contents == null) return false;
		RelicBundleContentsComponent.Builder builder = new RelicBundleContentsComponent.Builder(contents);
		if (clickType == ClickType.LEFT) {
			ItemStack slotStack = slot.getStack();
			if (!slotStack.isEmpty()) {
				if (contents.isPresent()) {
					return false;
				} else if (!slotStack.isIn(TagRegistry.Item.relic)) {
					playFailSound(player);
					return false;
				} else {
					ItemStack single = slotStack.split(1);
					builder.set(single);
					playInsertSound(player);
				}
			} else return false;
		} else if (clickType == ClickType.RIGHT) {
			ItemStack removed = builder.remove();
			if (removed != null) {
				ItemStack slotStack = slot.getStack();
				if (slotStack.isEmpty()) {
					slot.setStack(removed);
					playRemoveSound(player);
				} else if (ItemStack.areItemsAndComponentsEqual(slotStack, removed) && slotStack.getCount() < slotStack.getMaxCount()) {
					slotStack.increment(1);
					playRemoveSound(player);
				} else {
					builder.set(removed);
					playFailSound(player);
					return false;
				}
			} else return false;
		} else return false;
		relicBundle.set(ComponentTypeRegistry.relicBundleContents, builder.build());
		return true;
	}


	public boolean onClicked(ItemStack relicBundle, ItemStack slotStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
		RelicBundleContentsComponent contents = getContents(relicBundle);
		if (contents == null || !contents.getStack().isEmpty() || slotStack.isEmpty()) return false;
		RelicBundleContentsComponent.Builder builder = new RelicBundleContentsComponent.Builder(contents);
		if (clickType == ClickType.LEFT) {
			if (contents.isPresent() || !slotStack.isIn(TagRegistry.Item.relic)) {
				playFailSound(player);
				return false;
			} else {
				builder.set(slotStack.split(1));
				playInsertSound(player);
			}
			relicBundle.set(ComponentTypeRegistry.relicBundleContents, builder.build());
			return true;
		}
		return false;
	}

	public void onItemEntityDestroyed(ItemEntity entity) {
		RelicBundleContentsComponent contents = entity.getStack().get(ComponentTypeRegistry.relicBundleContents);
		if (contents != null && contents.isPresent()) {
			entity.getStack().set(ComponentTypeRegistry.relicBundleContents, RelicBundleContentsComponent.empty);
			ItemUsage.spawnItemContents(entity, Stream.of(contents.getStack()).toList());
		}
	}

	private static void playRemoveSound(Entity entity) {
		entity.playSound(SoundEvents.ITEM_BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
	}

	private static void playInsertSound(Entity entity) {
		entity.playSound(SoundEvents.ITEM_BUNDLE_INSERT, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
	}

	private static void playFailSound(Entity entity) {
		entity.playSound(SoundEvents.ITEM_BUNDLE_INSERT_FAIL, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
	}

	public Optional<TooltipData> getTooltipData(ItemStack stack) {
		TooltipDisplayComponent tooltipDisplayComponent = stack.getOrDefault(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplayComponent.DEFAULT);
		return !tooltipDisplayComponent.shouldDisplay(ComponentTypeRegistry.relicBundleContents) ? Optional.empty() : Optional.ofNullable(stack.get(ComponentTypeRegistry.relicBundleContents)).map(RelicBundleTooltipData::new);
	}

	public RelicLoader getLoader() {
		return Relic.data;
	}
}
