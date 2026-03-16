/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry;

import dev.dannytaylor.dtaf2026.common.data.Data;
import dev.dannytaylor.dtaf2026.common.registry.entity.SomniumRealeServerPlayerEntity;
import dev.dannytaylor.dtaf2026.common.registry.entity.Timer;
import dev.dannytaylor.dtaf2026.common.registry.entity.boar.BoarVariants;
import dev.dannytaylor.dtaf2026.common.registry.entity.junglefowl.JunglefowlVariants;
import dev.dannytaylor.dtaf2026.common.registry.networking.CreditsScreenS2CPacket;
import dev.dannytaylor.dtaf2026.common.registry.networking.EventS2CPacket;
import dev.dannytaylor.dtaf2026.common.registry.networking.VariantsS2CPacket;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NetworkingRegistry {
	public static final Identifier variants;
	public static final Identifier creditsScreen;
	public static final Identifier worldEvent;

	static {
		variants = Data.idOf("variants");
		creditsScreen = Data.idOf("credits_screen");
		worldEvent = Data.idOf("world_event");
	}

	public static void bootstrap() {
		PayloadTypeRegistry.playS2C().register(VariantsS2CPacket.id, VariantsS2CPacket.packetCodec);
		PayloadTypeRegistry.playS2C().register(CreditsScreenS2CPacket.id, CreditsScreenS2CPacket.packetCodec);
		PayloadTypeRegistry.playS2C().register(EventS2CPacket.id, EventS2CPacket.packetCodec);
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> sendJunglefowlVariants(handler.player));
		ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, manager, success) -> sendJunglefowlVariants(server));
	}

	public static void sendCreditsScreen(ServerPlayerEntity player, long ms) {
		try {
			boolean seenScreen = ((SomniumRealeServerPlayerEntity) player).dtaf2026$getSeenModCredits();
			if (!seenScreen) {
				player.detach();
				player.getWorld().removePlayer(player, Entity.RemovalReason.CHANGED_DIMENSION);
				if (!player.notInAnyWorld) {
					player.notInAnyWorld = true;
				}
				((SomniumRealeServerPlayerEntity) player).dtaf2026$setSeenModCredits(true);
			}
			ServerPlayNetworking.send(player, new CreditsScreenS2CPacket(seenScreen, Timer.formatTime(ms)));
		} catch (Exception error) {
			Data.getLogger().warn("Caught error in credits screen packet to {}: {}", player.getGameProfile().getName(), error);
		}
	}

	public static void sendJunglefowlVariants(MinecraftServer server) {
		server.getPlayerManager().getPlayerList().forEach(NetworkingRegistry::sendJunglefowlVariants);
	}

	public static void sendJunglefowlVariants(ServerPlayerEntity player) {
		try {
			ServerPlayNetworking.send(player, new VariantsS2CPacket(JunglefowlVariants.variants.getRegistry(), BoarVariants.variants.getRegistry()));
		} catch (Exception error) {
			Data.getLogger().warn("Caught error in junglefowl variants packet to {}: {}", player.getGameProfile().getName(), error);
		}
	}

	public static void sendWorldEvent(List<ServerPlayerEntity> players, int eventId, BlockPos pos, int data, boolean global) {
		players.forEach((serverPlayer) -> NetworkingRegistry.sendWorldEvent(serverPlayer, eventId, pos, data, global));
	}

	public static void sendWorldEvent(ServerPlayerEntity player, int eventId, BlockPos pos, int data, boolean global) {
		try {
			ServerPlayNetworking.send(player, new EventS2CPacket(eventId, pos, data, global));
		} catch (Exception error) {
			Data.getLogger().warn("Caught error in junglefowl variants packet to {}: {}", player.getGameProfile().getName(), error);
		}
	}

	public static List<ServerPlayerEntity> getAround(PlayerManager manager, @Nullable PlayerEntity player, double x, double y, double z, double distance, RegistryKey<World> worldKey) {
		List<ServerPlayerEntity> players = new ArrayList<>();
		for (int i = 0; i < manager.getPlayerList().size(); i++) {
			ServerPlayerEntity serverPlayerEntity = manager.getPlayerList().get(i);
			if (serverPlayerEntity != player && serverPlayerEntity.getWorld().getRegistryKey() == worldKey) {
				double d = x - serverPlayerEntity.getX();
				double e = y - serverPlayerEntity.getY();
				double f = z - serverPlayerEntity.getZ();
				if (d * d + e * e + f * f < distance * distance) {
					players.add(serverPlayerEntity);
				}
			}
		}
		return players;
	}
}
