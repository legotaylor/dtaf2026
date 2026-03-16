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

public record CreditsScreenS2CPacket(boolean seenScreen, String time) implements CustomPayload {
	public static final Id<CreditsScreenS2CPacket> id = new Id<>(NetworkingRegistry.creditsScreen);
	public static final PacketCodec<RegistryByteBuf, CreditsScreenS2CPacket> packetCodec = PacketCodec.tuple(PacketCodecs.BOOLEAN, CreditsScreenS2CPacket::seenScreen, PacketCodecs.STRING, CreditsScreenS2CPacket::time, CreditsScreenS2CPacket::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return id;
	}
}
