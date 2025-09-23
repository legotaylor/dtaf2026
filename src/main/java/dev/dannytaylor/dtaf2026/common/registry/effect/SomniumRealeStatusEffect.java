/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.particle.ParticleEffect;

public class SomniumRealeStatusEffect extends StatusEffect {
	public SomniumRealeStatusEffect(StatusEffectCategory category, int color) {
		super(category, color);
	}

	public SomniumRealeStatusEffect(StatusEffectCategory category, int color, ParticleEffect particleEffect) {
		super(category, color, particleEffect);
	}
}
