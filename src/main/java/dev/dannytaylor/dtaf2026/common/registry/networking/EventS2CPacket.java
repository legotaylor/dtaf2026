/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;

public record EventS2CPacket(int eventId, BlockPos pos, int data, boolean global) implements CustomPayload {
	public static final Id<EventS2CPacket> id = new Id<>(NetworkingRegistry.worldEvent);
	public static final PacketCodec<RegistryByteBuf, EventS2CPacket> packetCodec = PacketCodec.tuple(PacketCodecs.INTEGER, EventS2CPacket::eventId, BlockPos.PACKET_CODEC, EventS2CPacket::pos, PacketCodecs.INTEGER, EventS2CPacket::data, PacketCodecs.BOOLEAN, EventS2CPacket::global, EventS2CPacket::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return id;
	}
}
