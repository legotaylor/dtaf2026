/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.mixin.zoom;

import dev.dannytaylor.dtaf2026.client.config.Config;
import dev.dannytaylor.dtaf2026.client.registry.keybinds.Zoom;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.input.Scroller;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector2i;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public abstract class MouseMixin {
	@Shadow
	@Final
	private MinecraftClient client;
	@Shadow
	@Final
	private Scroller scroller;

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isSpectator()Z"), method = "onMouseScroll", cancellable = true)
	private void dtaf2026$onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
		if (Zoom.isActive()) {
			boolean discreteMouseScroll = this.client.options.getDiscreteMouseScroll().getValue();
			double mouseWheelSensitivity = this.client.options.getMouseWheelSensitivity().getValue();
			double calculatedScroll = (discreteMouseScroll ? Math.signum(vertical) : vertical) * mouseWheelSensitivity;
			Vector2i vector2i = this.scroller.update(calculatedScroll, calculatedScroll);
			if (vector2i.y != 0) {
				Zoom.adjustZoomLevel(vector2i.y, Config.instance.zoomAdjustmentMultiplier.value());
				ci.cancel();
			}
		}
	}

	@Inject(at = @At("HEAD"), method = "onMouseButton", cancellable = true)
	private void dtaf2026$onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
		if (Zoom.isActive()) {
			if (button == 2) {
				Zoom.resetZoomLevel();
				ci.cancel();
			}
		}
	}

	@ModifyVariable(method = "updateMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/tutorial/TutorialManager;onUpdateMouse(DD)V"), ordinal = 1)
	private double dtaf2026$updateXSensitivity(double x) {
		if (Zoom.isActive()) {
			if (this.client.player != null) {
				double angle = MathHelper.cos((this.client.player.getPitch() / 180.0F) * MathHelper.PI);
				x = (x * (1.0F / Math.max((angle < 0) ? angle * -1.0F : angle, (Math.max(Zoom.getMultiplierFromFOV(), 0.0F) + 1.0F) / 11.0F))) * Zoom.getMultiplierFromFOV();
			}
		}
		return x;
	}

	@ModifyVariable(method = "updateMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/tutorial/TutorialManager;onUpdateMouse(DD)V"), ordinal = 2)
	private double dtaf2026$updateYSensitivity(double y) {
		return Zoom.isActive() ? y * Zoom.getMultiplierFromFOV() : y;
	}
}
