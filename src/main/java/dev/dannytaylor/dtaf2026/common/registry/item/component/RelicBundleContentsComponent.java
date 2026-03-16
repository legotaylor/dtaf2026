/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public record RelicBundleContentsComponent(ItemStack stack) implements TooltipData {
	public static final Codec<RelicBundleContentsComponent> codec;
	public static final PacketCodec<RegistryByteBuf, RelicBundleContentsComponent> packetCodec;
	public static final RelicBundleContentsComponent empty = new RelicBundleContentsComponent(ItemStack.EMPTY);

	public RelicBundleContentsComponent(ItemStack stack) {
		if (stack != null && stack.getCount() > 1) throw new IllegalArgumentException("RelicBundleContentsComponent can only hold one id of size 1");
		this.stack = stack == null ? ItemStack.EMPTY : stack.copy();
	}

	public static DataResult<RelicBundleContentsComponent> validate(ItemStack stack) {
		if (stack == null || stack.isEmpty()) return DataResult.success(empty);
		if (stack.getCount() != 1) return DataResult.error(() -> "Stack size must be 1");
		return DataResult.success(new RelicBundleContentsComponent(stack));
	}

	public ItemStack getStack() {
		return this.stack.copy();
	}

	public boolean isEmpty() {
		return this.stack.isEmpty();
	}

	public boolean isPresent() {
		return !isEmpty();
	}

	public Stream<ItemStack> stream() {
		return this.stack.isEmpty() ? Stream.empty() : Stream.of(this.stack.copy());
	}

	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof RelicBundleContentsComponent(ItemStack stack1))) return false;
		return ItemStack.areItemsEqual(this.stack, stack1);
	}

	public @NotNull String toString() {
		return "RelicBundleContentsComponent[stack=" + this.stack + "]";
	}

	static {
		codec = ItemStack.CODEC.flatXmap(RelicBundleContentsComponent::validate, component -> DataResult.success(component.stack));
		packetCodec = ItemStack.PACKET_CODEC.xmap(RelicBundleContentsComponent::new, component -> component.stack);
	}

	public static class Builder {
		private ItemStack stack;

		public Builder(RelicBundleContentsComponent base) {
			this.stack = base.stack.copy();
		}

		public Builder clear() {
			this.stack = ItemStack.EMPTY;
			return this;
		}

		public boolean set(ItemStack stack) {
			if (stack == null || stack.isEmpty() || stack.getCount() != 1) return false;
			this.stack = stack.copy();
			return true;
		}

		public ItemStack remove() {
			if (this.stack.isEmpty()) return null;
			ItemStack temp = this.stack.copy();
			this.stack = ItemStack.EMPTY;
			return temp;
		}

		public RelicBundleContentsComponent build() {
			return new RelicBundleContentsComponent(this.stack);
		}
	}
}
