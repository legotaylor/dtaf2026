/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.mixin.effect.sure_landing;

import dev.dannytaylor.dtaf2026.common.registry.StatusEffectRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
	@Shadow public abstract boolean hasStatusEffect(RegistryEntry<StatusEffect> effect);
	@Shadow protected abstract void playBlockFallSound();

	@Inject(method = "computeFallDamage", at = @At("HEAD"), cancellable = true)
	public void dtaf2026$computeFallDamage(double fallDistance, float damagePerDistance, CallbackInfoReturnable<Integer> cir) {
		if (this.hasStatusEffect(StatusEffectRegistry.sureLanding)) {
			this.playBlockFallSound(); // We prevent the damage, but we still want the sound.
			cir.setReturnValue(0);
		}
	}
}
