/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry;

import dev.dannytaylor.dtaf2026.common.registry.advancement.AdvancementRegistry;
import dev.dannytaylor.dtaf2026.common.registry.block.BlockRegistry;
import dev.dannytaylor.dtaf2026.common.registry.block.FlammableRegistry;
import dev.dannytaylor.dtaf2026.common.registry.effect.StatusEffectRegistry;
import dev.dannytaylor.dtaf2026.common.registry.entity.*;
import dev.dannytaylor.dtaf2026.common.registry.item.FuelRegistry;
import dev.dannytaylor.dtaf2026.common.registry.item.ItemRegistry;
import dev.dannytaylor.dtaf2026.common.registry.networking.NetworkingRegistry;
import dev.dannytaylor.dtaf2026.common.registry.particle.ParticleRegistry;
import dev.dannytaylor.dtaf2026.common.registry.relic.Relic;
import dev.dannytaylor.dtaf2026.common.registry.sound.SoundEventRegistry;
import dev.dannytaylor.dtaf2026.common.registry.stats.StatRegistry;
import dev.dannytaylor.dtaf2026.common.registry.worldgen.WorldGenRegistry;
import net.minecraft.server.world.ServerWorld;

public class Registry {
	public static void bootstrap() {
		AdvancementRegistry.bootstrap();
		StatRegistry.bootstrap();
		SoundEventRegistry.bootstrap();
		DataComponentRegistry.bootstrap();
		TrackedDataRegistry.bootstrap();
		ParticleRegistry.bootstrap();
		WaypointStyleRegistry.bootstrap();
		EntityRegistry.bootstrap();
		BlockRegistry.bootstrap();
		ItemRegistry.bootstrap();
		FuelRegistry.bootstrap();
		FlammableRegistry.bootstrap();
		WorldGenRegistry.bootstrap();
		AttributeModifierRegistry.bootstrap();
		StatusEffectRegistry.bootstrap();
		Relic.bootstrap();
		NetworkingRegistry.bootstrap();
	}

	public static void worldTick(ServerWorld world) {
		Timer.tick(world);
	}
}
