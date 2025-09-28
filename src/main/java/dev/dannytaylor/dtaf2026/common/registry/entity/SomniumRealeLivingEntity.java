/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.entity;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.entry.RegistryEntry;

public interface SomniumRealeLivingEntity {
	void dtaf2026$updateAttribute(RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier modifier, ShouldApply shouldApply);
	void dtaf2026$updateAttribute(RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier modifier, RegistryEntry<StatusEffect> statusEffect);
	boolean dtaf2026$isInSomniumReale();

	@FunctionalInterface
	interface ShouldApply {
		boolean get();
	}
}
