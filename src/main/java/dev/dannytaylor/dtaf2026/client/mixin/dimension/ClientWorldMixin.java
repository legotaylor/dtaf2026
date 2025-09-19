/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.mixin.dimension;

import dev.dannytaylor.dtaf2026.client.registry.ClientDimensionRegistry;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin {
	@ModifyArg(method = "getSkyColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;multiply(D)Lnet/minecraft/util/math/Vec3d;", ordinal = 1))
	private double dtaf2026$getSkyColor(double value) {
		return ClientDimensionRegistry.isSomniumReale() ? 1.0F : value;
	}
}
