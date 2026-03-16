/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.mixin.gui;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import dev.dannytaylor.dtaf2026.common.registry.ItemRegistry;
import dev.dannytaylor.dtaf2026.common.registry.WaypointStyleRegistry;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.bar.LocatorBar;
import net.minecraft.util.Identifier;
import net.minecraft.world.waypoint.Waypoint;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = LocatorBar.class, priority = 100)
public abstract class LocatorBarMixin {
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIIII)V"), method = "method_70870")
	private void dtaf2026$render(DrawContext instance, RenderPipeline pipeline, Identifier sprite, int x, int y, int width, int height, int color, @Local(name = "config") Waypoint.Config config) {
		if (config.style.equals(WaypointStyleRegistry.fleeciferBoss)) {
			Matrix3x2fStack matrices = instance.getMatrices();
			matrices.pushMatrix();
			float scale = 0.5F;
			matrices.translate(x, y);
			matrices.scale(scale, scale);
			instance.drawItemWithoutEntity(ItemRegistry.eyeOfFleecifer.getDefaultStack(), 0, 0);
			matrices.popMatrix();
		} else instance.drawGuiTexture(pipeline, sprite, x, y, width, height, color);
	}
}
