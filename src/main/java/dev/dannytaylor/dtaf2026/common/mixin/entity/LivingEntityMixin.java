/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.mixin.entity;

import dev.dannytaylor.dtaf2026.common.registry.effect.StatusEffectRegistry;
import dev.dannytaylor.dtaf2026.common.registry.tagkey.TagRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Shadow public abstract boolean hasStatusEffect(RegistryEntry<StatusEffect> effect);
	@Shadow protected abstract void playBlockFallSound();
	@Shadow @Nullable public abstract EntityAttributeInstance getAttributeInstance(RegistryEntry<EntityAttribute> attribute);

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "computeFallDamage", at = @At("HEAD"), cancellable = true)
	public void dtaf2026$computeFallDamage(double fallDistance, float damagePerDistance, CallbackInfoReturnable<Integer> cir) {
		if (this.hasStatusEffect(StatusEffectRegistry.safeLanding)) {
			this.playBlockFallSound(); // We prevent the damage, but we still want the sound.
			cir.setReturnValue(0);
		}
	}

	@Inject(method = "getScale", at = @At("RETURN"), cancellable = true)
	public void dtaf2026$getScale(CallbackInfoReturnable<Float> cir) {
		cir.setReturnValue(this.dtaf2026$updateScales(cir.getReturnValue()));
	}

	@Unique
	private float dtaf2026$updateScales(float scale) {
		scale = dtaf2026$updateScale(scale, -0.35F, ((LivingEntity) (Object) this) instanceof PlayerEntity && this.dtaf2026$isInAbstractSomniumReale());
		scale = dtaf2026$updateScale(scale, 0.35F, StatusEffectRegistry.growth);
		scale = dtaf2026$updateScale(scale, -0.35F, StatusEffectRegistry.shrink);
		return scale;
	}

	@Unique
	public float dtaf2026$updateScale(float scale, float add, boolean shouldApply) {
		return shouldApply ? scale + add : scale;
	}

	@Unique
	public float dtaf2026$updateScale(float scale, float add, RegistryEntry<StatusEffect> statusEffect) {
		return dtaf2026$updateScale(scale, add, this.hasStatusEffect(statusEffect));
	}

	@Unique
	public boolean dtaf2026$isInAbstractSomniumReale() {
		return TagRegistry.WorldGen.Biome.isIn(this.getWorld(), this.getBlockPos(), TagRegistry.WorldGen.Biome.abstractSomniumReale);
	}
}
