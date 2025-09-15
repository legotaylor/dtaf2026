/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.mixin.gui;

import dev.dannytaylor.dtaf2026.client.gui.TitleScreenHelper;
import net.minecraft.client.gui.screen.AccessibilityOnboardingScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AccessibilityOnboardingScreen.class, priority = 100)
public abstract class AccessibilityOnboardingScreenMixin {
	@Inject(at = @At("HEAD"), method = "init")
	private void dtaf2025$init(CallbackInfo ci) {
		TitleScreenHelper.showFlashingLightsWarning();
	}
}
