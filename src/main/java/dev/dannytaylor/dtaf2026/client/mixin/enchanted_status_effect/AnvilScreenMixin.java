/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.mixin.enchanted_status_effect;

import dev.dannytaylor.dtaf2026.common.registry.StatusEffectRegistry;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AnvilScreen.class)
public abstract class AnvilScreenMixin {
	@Shadow @Final private PlayerEntity player;

	@Redirect(method = "drawForeground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)V"))
	public void onTakeOutput(DrawContext context, TextRenderer textRenderer, Text text, int x, int y, int color) {
		if (this.player.hasStatusEffect(StatusEffectRegistry.enchanted)) text = text.copy().formatted(Formatting.STRIKETHROUGH);
		context.drawTextWithShadow(textRenderer, text, x, y, color);
	}
}
