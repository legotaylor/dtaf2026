/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.particle;

import dev.dannytaylor.dtaf2026.common.data.Data;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ParticleRegistry {
	public static final SimpleParticleType mapleLeaves;
	public static final SimpleParticleType ceruleanLeaves;

	static {
		mapleLeaves = register("maple_leaves", false);
		ceruleanLeaves = register("cerulean_leaves", false);
	}

	public static void bootstrap() {
	}

	private static SimpleParticleType register(Identifier id, boolean alwaysShow) {
		return Registry.register(Registries.PARTICLE_TYPE, id, FabricParticleTypes.simple(alwaysShow));
	}

	private static SimpleParticleType register(String path, boolean alwaysShow) {
		return register(Data.idOf(path), alwaysShow);
	}
}
