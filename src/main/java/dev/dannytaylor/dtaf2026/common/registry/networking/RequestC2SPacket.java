/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record RequestC2SPacket(Identifier requestId) implements CustomPayload {
	public static final Id<RequestC2SPacket> id = new Id<>(NetworkingRegistry.request);
	public static final PacketCodec<RegistryByteBuf, RequestC2SPacket> packetCodec = PacketCodec.tuple(Identifier.PACKET_CODEC, RequestC2SPacket::requestId, RequestC2SPacket::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return id;
	}
}
