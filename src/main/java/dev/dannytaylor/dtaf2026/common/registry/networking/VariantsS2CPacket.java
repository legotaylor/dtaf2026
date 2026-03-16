/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.networking;

import dev.dannytaylor.dtaf2026.common.registry.NetworkingRegistry;
import dev.dannytaylor.dtaf2026.common.registry.entity.boar.BoarVariant;
import dev.dannytaylor.dtaf2026.common.registry.entity.junglefowl.JunglefowlVariant;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public record VariantsS2CPacket(Map<Identifier, JunglefowlVariant> junglefowl,
								Map<Identifier, BoarVariant> boar) implements CustomPayload {
	public static final Id<VariantsS2CPacket> id = new Id<>(NetworkingRegistry.variants);
	public static final PacketCodec<RegistryByteBuf, VariantsS2CPacket> packetCodec = PacketCodec.tuple(PacketCodecs.map(HashMap::new, Identifier.PACKET_CODEC, JunglefowlVariant.packetCodec), VariantsS2CPacket::junglefowl, PacketCodecs.map(HashMap::new, Identifier.PACKET_CODEC, BoarVariant.packetCodec), VariantsS2CPacket::boar, VariantsS2CPacket::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return id;
	}
}
