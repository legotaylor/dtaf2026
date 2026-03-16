/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.mixin.enchanted_status_effect;

import dev.dannytaylor.dtaf2026.common.registry.StatusEffectRegistry;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.EnchantmentScreen;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EnchantmentScreen.class)
public abstract class EnchantmentScreenMixin extends Screen {
	protected EnchantmentScreenMixin(Text title) {
		super(title);
	}

	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/text/MutableText;formatted(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/MutableText;", ordinal = 3))
	public MutableText onTakeOutput(MutableText instance, Formatting formatting) {
		assert this.client != null;
		assert this.client.player != null;
		MutableText text = instance.formatted(formatting);
		if (this.client.player.hasStatusEffect(StatusEffectRegistry.enchanted)) text.formatted(Formatting.STRIKETHROUGH);
		return text;
	}
}
