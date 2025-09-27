/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.mixin.effect.enchanted;

import dev.dannytaylor.dtaf2026.common.registry.StatusEffectRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@ModifyVariable(method = "applyEnchantmentCosts", at = @At("HEAD"), index = 2, argsOnly = true)
	public int dtaf2026$applyEnchantmentCosts(int value) {
		return this.hasStatusEffect(StatusEffectRegistry.enchanted) ? 0 : value;
	}
}
