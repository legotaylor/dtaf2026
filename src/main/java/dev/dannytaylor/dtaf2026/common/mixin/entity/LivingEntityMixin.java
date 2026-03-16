/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.mixin.entity;

import dev.dannytaylor.dtaf2026.common.registry.effect.StatusEffectRegistry;
import dev.dannytaylor.dtaf2026.common.registry.entity.AttributeModifierRegistry;
import dev.dannytaylor.dtaf2026.common.registry.entity.SomniumRealeLivingEntity;
import dev.dannytaylor.dtaf2026.common.registry.tagkey.TagRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements SomniumRealeLivingEntity {
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

	@Inject(method = "tick", at = @At("RETURN"))
	public void dtaf2026$tick(CallbackInfo ci) {
		this.dtaf2026$updateStatusEffects();
	}

	@Unique
	private void dtaf2026$updateStatusEffects() {
		dtaf2026$updateAttribute(EntityAttributes.SCALE, AttributeModifierRegistry.growth, StatusEffectRegistry.growth);
		dtaf2026$updateAttribute(EntityAttributes.SCALE, AttributeModifierRegistry.shrink, StatusEffectRegistry.shrink);
	}

	public void dtaf2026$updateAttribute(RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier modifier, ShouldApply shouldApply) {
		EntityAttributeInstance entityAttributeInstance = this.getAttributeInstance(attribute);
		if (entityAttributeInstance != null) {
			entityAttributeInstance.removeModifier(modifier.id());
			if (shouldApply.get()) entityAttributeInstance.addTemporaryModifier(modifier);
		}
	}

	public void dtaf2026$updateAttribute(RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier modifier, RegistryEntry<StatusEffect> statusEffect) {
		dtaf2026$updateAttribute(attribute, modifier, () -> this.hasStatusEffect(statusEffect));
	}

	public boolean dtaf2026$isInAbstractSomniumReale() {
		return TagRegistry.WorldGen.Biome.isIn(this.getWorld(), this.getBlockPos(), TagRegistry.WorldGen.Biome.abstractSomniumReale);
	}

	public boolean dtaf2026$isInSomniumReale() {
		return TagRegistry.WorldGen.Biome.isIn(this.getWorld(), this.getBlockPos(), TagRegistry.WorldGen.Biome.somniumReale);
	}

	public boolean dtaf2026$isInTheTerrorlands() {
		return TagRegistry.WorldGen.Biome.isIn(this.getWorld(), this.getBlockPos(), TagRegistry.WorldGen.Biome.theTerrorlands);
	}
}
