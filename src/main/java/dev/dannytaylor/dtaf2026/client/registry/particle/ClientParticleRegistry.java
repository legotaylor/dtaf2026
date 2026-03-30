/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry.particle;

import dev.dannytaylor.dtaf2026.common.registry.particle.ParticleRegistry;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.FlameParticle;

public class ClientParticleRegistry {
	public static void bootstrap() {
		ParticleFactoryRegistry.getInstance().register(ParticleRegistry.mapleLeaves, LeavesParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ParticleRegistry.ceruleanLeaves, LeavesParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ParticleRegistry.aerostoneFlame, FlameParticle.Factory::new);
	}
}
