/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.mixin.dimension;

import dev.dannytaylor.dtaf2026.client.config.Config;
import dev.dannytaylor.dtaf2026.common.registry.AttributeModifierRegistry;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
	@Redirect(method = "updateFovMultiplier", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;getFovMultiplier(ZF)F"))
	private float dtaf2025$updateFovMultiplier(AbstractClientPlayerEntity player, boolean firstPerson, float fovEffectScale) {
		float fov = player.getFovMultiplier(firstPerson, fovEffectScale);
		if (Config.instance.scaleFOV.value()) {
			EntityAttributeInstance entityAttributeInstance = player.getAttributeInstance(EntityAttributes.SCALE);
			if (entityAttributeInstance != null) {
				EntityAttributeModifier biomeModifier = entityAttributeInstance.getModifier(AttributeModifierRegistry.somniumRealeBiomeModifier.id());
				if (biomeModifier != null) {
					fov = (float) Math.toDegrees(2.0F * Math.atan((1.0F + (biomeModifier.value() - 1.0F) * Config.instance.scaleFOVStrength.value()) * Math.tan(Math.toRadians(fov / 2.0F))));
				}
			}
		}
		return fov;
	}
}
