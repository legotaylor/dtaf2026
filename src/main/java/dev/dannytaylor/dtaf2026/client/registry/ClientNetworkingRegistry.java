/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry;

import dev.dannytaylor.dtaf2026.client.data.ClientData;
import dev.dannytaylor.dtaf2026.client.gui.screen.CreditsScreen;
import dev.dannytaylor.dtaf2026.client.registry.entity.ClientVariantRegistries;
import dev.dannytaylor.dtaf2026.common.registry.ItemRegistry;
import dev.dannytaylor.dtaf2026.common.registry.networking.CreditsScreenS2CPacket;
import dev.dannytaylor.dtaf2026.common.registry.networking.EventS2CPacket;
import dev.dannytaylor.dtaf2026.common.registry.networking.Events;
import dev.dannytaylor.dtaf2026.common.registry.networking.VariantsS2CPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClientStatusC2SPacket;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

public class ClientNetworkingRegistry {
	public static void bootstrap() {
		ClientPlayNetworking.registerGlobalReceiver(VariantsS2CPacket.id, (payload, context) -> ClientData.getMinecraft().execute(() -> ClientVariantRegistries.instance.setAll(payload.junglefowl(), payload.boar())));
		ClientPlayNetworking.registerGlobalReceiver(CreditsScreenS2CPacket.id, (payload, context) -> {
			if (!payload.seenScreen()) ClientData.getMinecraft().setScreen(new CreditsScreen("somnium_reale", () -> {
				if (ClientData.getMinecraft().player != null)
					ClientData.getMinecraft().player.networkHandler.sendPacket(new ClientStatusC2SPacket(ClientStatusC2SPacket.Mode.PERFORM_RESPAWN));
				ClientData.getMinecraft().setScreen(null);
			}, payload.time()));
		});
		ClientPlayNetworking.registerGlobalReceiver(EventS2CPacket.id, (payload, context) -> processEvent(context.client(), payload.eventId(), payload.pos(), payload.data(), payload.global()));
	}

	public static void processEvent(MinecraftClient client, int eventId, BlockPos pos, int data, boolean global) {
		switch (eventId) {
			case Events.fleeciferEyeBreak:
				Random random = Random.create();
				double d = pos.getX() + 0.5;
				double e = pos.getY();
				double f = pos.getZ() + 0.5;

				for (int i = 0; i < 8; i++) {
					client.worldRenderer
						.addParticle(
							new ItemStackParticleEffect(ParticleTypes.ITEM, new ItemStack(ItemRegistry.eyeOfFleecifer)),
							d,
							e,
							f,
							random.nextGaussian() * 0.15,
							-1.0 + (random.nextDouble() * 0.2),
							random.nextGaussian() * 0.15
						);
				}

				for (double g = 0.0; g < Math.PI * 2; g += Math.PI / 20) {
					summonParticles(ParticleTypes.PORTAL, client, d, e, f, g);
					summonParticles(ParticleTypes.ELECTRIC_SPARK, client, d, e, f, g);
					summonParticles(ParticleTypes.ASH, client, d, e, f, g);
				}
				break;
		}
	}

	private static <T extends ParticleEffect> void summonParticles(T particleType, MinecraftClient client, double d, double e, double f, double g) {
		client.worldRenderer.addParticle(particleType, d + Math.cos(g) * 5.0, e - 0.4, f + Math.sin(g) * 5.0, Math.cos(g) * -5.0, -1.0, Math.sin(g) * -5.0);
		client.worldRenderer.addParticle(particleType, d + Math.cos(g) * 5.0, e - 0.4, f + Math.sin(g) * 5.0, Math.cos(g) * -7.0, -1.0, Math.sin(g) * -7.0);
	}
}
