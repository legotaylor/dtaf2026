/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry;

import dev.dannytaylor.dtaf2026.common.data.Data;
import dev.dannytaylor.dtaf2026.common.registry.effect.SomniumRealeStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class StatusEffectRegistry {
	public static final RegistryEntry<StatusEffect> enchanted;

	public static RegistryEntry<StatusEffect> register(Identifier id, StatusEffect statusEffect) {
		return Registry.registerReference(Registries.STATUS_EFFECT, id, statusEffect);
	}

	public static RegistryEntry<StatusEffect> register(String id, StatusEffect statusEffect) {
		return register(Data.idOf(id), statusEffect);
	}

	public static void bootstrap() {
	}

	static {
		enchanted = register("enchanted", new SomniumRealeStatusEffect(StatusEffectCategory.BENEFICIAL, 9347907));
	}
}
