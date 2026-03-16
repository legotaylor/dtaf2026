/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.mixin.zoom;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.blaze3d.systems.ProjectionType;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.dannytaylor.dtaf2026.client.registry.keybinds.Zoom;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.ProjectionMatrix3;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
	@Shadow
	@Final
	private MinecraftClient client;
	@Shadow
	@Final
	private ProjectionMatrix3 hudProjectionMatrix;
	@Unique
	private float fov;

	@Shadow
	public abstract boolean isRenderingPanorama();

	@Inject(method = "updateFovMultiplier", at = @At("TAIL"))
	private void dtaf2026$updateFovMultiplier(CallbackInfo ci) {
		Zoom.updateMultiplier();
	}

	@ModifyArg(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/ProjectionMatrix3;set(IIF)Lcom/mojang/blaze3d/buffers/GpuBufferSlice;"))
	private float dtaf2026$fixHandFov(float fov) {
		this.fov = fov;
		return Zoom.fov;
	}

	@Inject(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;renderHand(FZLorg/joml/Matrix4f;)V", shift = At.Shift.AFTER))
	private void dtaf2026$fixFovAfterHand(RenderTickCounter renderTickCounter, CallbackInfo ci) {
		RenderSystem.setProjectionMatrix(this.hudProjectionMatrix.set(this.client.getWindow().getFramebufferWidth(), this.client.getWindow().getFramebufferHeight(), this.fov), ProjectionType.PERSPECTIVE);
	}

	@ModifyReturnValue(method = "getFov", at = @At("RETURN"))
	private float dtaf2026$getFov(float fov, Camera camera, float tickDelta, boolean changingFov) {
		if (camera != null) {
			Zoom.fov = fov;
			float newFOV = fov;
			if (!this.isRenderingPanorama()) {
				newFOV *= MathHelper.lerp(tickDelta, Zoom.prevMultiplier, Zoom.multiplier);
			}
			Zoom.zoomFov = Zoom.getLimitFOV(newFOV);
			return Zoom.zoomFov;
		}
		return fov;
	}

	@ModifyExpressionValue(method = "tiltViewWhenHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/SimpleOption;getValue()Ljava/lang/Object;"))
	private Object dtaf2026$getDamageTiltStrength(Object value) {
		return (value instanceof Double) ? ((Double) value) * Math.max(Zoom.getMultiplierFromFOV(), 0.001) : value;
	}

	@ModifyExpressionValue(method = "tiltViewWhenHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getDamageTiltYaw()F"))
	private float dtaf2026$getDamageTiltYaw(float value) {
		return (float) (value * Math.max(Zoom.getMultiplierFromFOV(), 0.001));
	}

	@Inject(method = "bobView", at = @At(value = "HEAD"), cancellable = true)
	private void dtaf2026$bobViewStrideDistance(MatrixStack matrices, float tickProgress, CallbackInfo ci) {
		if (this.client.player != null) {
			float f = this.client.player.distanceMoved - this.client.player.lastDistanceMoved;
			float g = -(this.client.player.distanceMoved + f * tickProgress);
			float h = (float) (MathHelper.lerp(tickProgress, this.client.player.lastStrideDistance, this.client.player.strideDistance) * Math.max(Zoom.multiplier, 0.001));
			matrices.translate(MathHelper.sin(g * 3.1415927F) * h * 0.5F, -Math.abs(MathHelper.cos(g * 3.1415927F) * h), 0.0F);
			matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.sin(g * 3.1415927F) * h * 3.0F));
			matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(Math.abs(MathHelper.cos(g * 3.1415927F - 0.2F) * h) * 5.0F));
		}
		ci.cancel();
	}
}
