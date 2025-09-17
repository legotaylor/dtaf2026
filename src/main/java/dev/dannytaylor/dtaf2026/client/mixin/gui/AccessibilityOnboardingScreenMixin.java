/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.mixin.gui;

import net.minecraft.client.gui.screen.AccessibilityOnboardingScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(AccessibilityOnboardingScreen.class)
public abstract class AccessibilityOnboardingScreenMixin {
	@ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/Positioner;margin(I)Lnet/minecraft/client/gui/widget/Positioner;"), method = "init")
	private int dtaf2026$adjustMargin(int value) {
		return 2;
	}
}
