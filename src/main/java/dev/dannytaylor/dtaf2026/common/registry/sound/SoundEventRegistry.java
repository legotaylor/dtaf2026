/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.sound;

import dev.dannytaylor.dtaf2026.common.data.Data;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class SoundEventRegistry {
	public static final SoundEvent entityJunglefowlAmbient;
	public static final SoundEvent entityJunglefowlDeath;
	public static final SoundEvent entityJunglefowlEgg;
	public static final SoundEvent entityJunglefowlHurt;
	public static final SoundEvent entityJunglefowlStep;
	public static final SoundEvent portalAmbient;
	public static final SoundEvent portalTeleport;
	public static final SoundEvent summonFleeciferBoss;
	public static final SoundEvent fleeciferEyeBreak;

	static {
		entityJunglefowlAmbient = register("entity.junglefowl.ambient");
		entityJunglefowlDeath = register("entity.junglefowl.death");
		entityJunglefowlEgg = register("entity.junglefowl.egg");
		entityJunglefowlHurt = register("entity.junglefowl.hurt");
		entityJunglefowlStep = register("entity.junglefowl.step");
		portalAmbient = register("portal_ambient");
		portalTeleport = register("portal_teleport");
		summonFleeciferBoss = register("summon_fleecifer_boss");
		fleeciferEyeBreak = register("fleecifer_eye_break");
	}

	public static SoundEvent register(String id) {
		return register(Data.idOf(id));
	}

	public static SoundEvent register(Identifier id) {
		return register(id, id);
	}

	public static SoundEvent register(Identifier id, Identifier soundId) {
		return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(soundId));
	}

	public static void bootstrap() {
	}
}
