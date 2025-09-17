/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.mixin.gui;

import dev.dannytaylor.dtaf2026.client.gui.ScreenHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Function;

@Mixin(value = MinecraftClient.class, priority = 100)
public abstract class MinecraftClientMixin {
	@Inject(at = @At("RETURN"), method = "createInitScreens", cancellable = true)
	private void dtaf2026$createInitScreens(List<Function<Runnable, Screen>> list, CallbackInfoReturnable<Boolean> cir) {
		if (ScreenHelper.showFlashingLightsWarning(list)) cir.setReturnValue(true);
	}
}
