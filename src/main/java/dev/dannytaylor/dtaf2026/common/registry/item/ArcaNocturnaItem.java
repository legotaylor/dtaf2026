/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.item;

import dev.dannytaylor.dtaf2026.common.registry.TagRegistry;
import dev.dannytaylor.dtaf2026.common.registry.item.component.ArcaNocturnaContentsComponent;
import dev.dannytaylor.dtaf2026.common.registry.item.component.RelicComponent;
import dev.dannytaylor.dtaf2026.common.registry.item.tooltip.ArcaNocturnaTooltipData;
import dev.dannytaylor.dtaf2026.common.registry.relic.RelicLoader;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
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

public class ArcaNocturnaItem extends Item {
	public ArcaNocturnaItem(Item.Settings settings) {
		super(settings);
	}

	public static ArcaNocturnaContentsComponent getContents(ItemStack arcaNocturnal) {
		return arcaNocturnal.get(ComponentTypeRegistry.arcaNocturnaContents);
	}

	public void inventoryTick(ItemStack arcaNocturnal, ServerWorld world, Entity entity, @Nullable EquipmentSlot slot) {
		super.inventoryTick(arcaNocturnal, world, entity, slot);
		if (entity instanceof LivingEntity livingEntity) {
			ArcaNocturnaContentsComponent contents = getContents(arcaNocturnal);
			if (contents.isPresent()) {
				RelicComponent relicComponent = contents.getStack().get(ComponentTypeRegistry.relic);
				if (relicComponent != null) RelicLoader.get(relicComponent.getId()).ifPresent((relic) -> relic.tick(livingEntity));
			}
		}
	}

	public boolean onStackClicked(ItemStack arcaNocturnal, Slot slot, ClickType clickType, PlayerEntity player) {
		ArcaNocturnaContentsComponent contents = getContents(arcaNocturnal);
		if (contents == null) return false;
		ArcaNocturnaContentsComponent.Builder builder = new ArcaNocturnaContentsComponent.Builder(contents);
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
		arcaNocturnal.set(ComponentTypeRegistry.arcaNocturnaContents, builder.build());
		return true;
	}

	public void onItemEntityDestroyed(ItemEntity entity) {
		ArcaNocturnaContentsComponent contents = entity.getStack().get(ComponentTypeRegistry.arcaNocturnaContents);
		if (contents != null && contents.isPresent()) {
			entity.getStack().set(ComponentTypeRegistry.arcaNocturnaContents, ArcaNocturnaContentsComponent.empty);
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
		return !tooltipDisplayComponent.shouldDisplay(ComponentTypeRegistry.arcaNocturnaContents) ? Optional.empty() : Optional.ofNullable(stack.get(ComponentTypeRegistry.arcaNocturnaContents)).map(ArcaNocturnaTooltipData::new);
	}
}
