/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import dev.dannytaylor.dtaf2026.common.data.Data;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public record RelicComponent(Identifier id) implements TooltipData {
	public static final Codec<RelicComponent> codec;
	public static final PacketCodec<ByteBuf, RelicComponent> packetCodec;
	public static final RelicComponent empty = new RelicComponent(Data.idOf("none"));

	public static DataResult<RelicComponent> validate(Identifier id) {
		return DataResult.success(new RelicComponent(id));
	}

	public Identifier getId() {
		return this.id();
	}

	public @NotNull String toString() {
		return "RelicComponent[id=" + id + "]";
	}

	static {
		codec = Identifier.CODEC.xmap(RelicComponent::new, RelicComponent::getId);
		packetCodec = Identifier.PACKET_CODEC.xmap(RelicComponent::new, RelicComponent::getId);
	}
}
