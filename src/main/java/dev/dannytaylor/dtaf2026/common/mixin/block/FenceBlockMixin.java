/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.mixin.block;

import dev.dannytaylor.dtaf2026.common.registry.block.SupportedFenceGateBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceBlock;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FenceBlock.class)
public abstract class FenceBlockMixin {
	@Inject(method = "canConnect", at = @At("RETURN"), cancellable = true)
	private static void dtaf2026$canConnect(BlockState state, boolean neighborIsFullSquare, Direction dir, CallbackInfoReturnable<Boolean> cir) {
		if (state.getBlock() instanceof SupportedFenceGateBlock && SupportedFenceGateBlock.canWallConnect(state, dir))
			cir.setReturnValue(true);
	}
}
