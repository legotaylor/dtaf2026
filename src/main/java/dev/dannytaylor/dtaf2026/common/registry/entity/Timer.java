/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.entity;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class Timer {
	public static String formatTime(long ms) {
		long hours = ms / 3600000;
		String hourStr = hours > 0 ? String.format("%02d:", hours) : "";
		long minutes = (ms % 3600000) / 60000;
		long seconds = (ms % 60000) / 1000;
		return String.format("%s%02d:%02d", hourStr, minutes, seconds);
	}

	public static void tick(ServerWorld world) {
		for (ServerPlayerEntity player : world.getPlayers()) {
			SomniumRealeServerPlayerEntity somniumRealePlayer = (SomniumRealeServerPlayerEntity) player;
			if (somniumRealePlayer.dtaf2026$getTimeRunning() && !world.getServer().isPaused()) {
				long now = System.currentTimeMillis();
				somniumRealePlayer.dtaf2026$setTime(somniumRealePlayer.dtaf2026$getTime() + (now - (somniumRealePlayer.dtaf2026$getLastTimeTick() == 0L ? System.currentTimeMillis() : somniumRealePlayer.dtaf2026$getLastTimeTick())));
				somniumRealePlayer.dtaf2026$setLastTimeTick(now);
			}
		}
	}
}
