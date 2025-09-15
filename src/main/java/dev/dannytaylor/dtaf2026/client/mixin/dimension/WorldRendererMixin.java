/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.mixin.dimension;

import dev.dannytaylor.dtaf2026.client.registry.dimension.SomniumRealeEffect;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
	@Shadow @Nullable private ClientWorld world;

	@ModifyArgs(method = "method_62215", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/SkyRendering;renderCelestialBodies(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;FIFF)V"))
	private void dtaf2026$brightenStarsInDimension(Args args) {
		if (this.world != null) {
			if (this.world.getDimensionEffects() instanceof SomniumRealeEffect) {
				//args.set(args.size() - 1, (float) args.get(args.size() - 1) * 2.0F);
			}
		}
	}
}
