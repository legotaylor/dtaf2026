/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.mixin.gui;

import com.llamalad7.mixinextras.sugar.Local;
import com.terraformersmc.modmenu.config.ModMenuConfig;
import dev.dannytaylor.dtaf2026.client.data.ClientData;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.math.ColorHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(value = TitleScreen.class, priority = 100)
public abstract class TitleScreenMixin extends Screen {
	protected TitleScreenMixin(Text title) {
		super(title);
	}

	@Redirect(method = "addNormalWidgets", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;build()Lnet/minecraft/client/gui/widget/ButtonWidget;"))
	private ButtonWidget dtaf2026$removeRealmsButton(ButtonWidget.Builder builder) {
		ButtonWidget button = builder.build();
		if (!FabricLoader.getInstance().isModLoaded("modmenu") || !ModMenuConfig.MODIFY_TITLE_SCREEN.getValue()) {
			if (button.getMessage().getContent() instanceof TranslatableTextContent translatableTextContent) {
				if (translatableTextContent.getKey().equals("menu.online")) {
					button.visible = false;
				}
			}
		}
		return button;
	}

	@Inject(method = "render", at = @At("RETURN"))
	private void dtaf2026$addNotAssociatedText(DrawContext context, int mouseX, int mouseY, float deltaTicks, CallbackInfo ci, @Local(name = "f") float f) {
		Text text = ClientData.getText("not_associated", ClientData.getText("name"));
		context.drawTextWithShadow(this.textRenderer, text, this.width - this.textRenderer.getWidth(text) - 2, this.height - 20, ColorHelper.withAlpha(f, -1));
	}

	@ModifyArgs(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/TitleScreen;addNormalWidgets(II)I"))
	private void dtaf2026$modifyY(Args args) {
		if (!FabricLoader.getInstance().isModLoaded("modmenu") || !ModMenuConfig.MODIFY_TITLE_SCREEN.getValue())
			args.set(0, ((int) args.get(0)) + 12);
	}

	@Inject(method = "addNormalWidgets", at = @At("RETURN"), cancellable = true)
	private void dtaf2026$modifyReturnY(int y, int spacingY, CallbackInfoReturnable<Integer> cir) {
		if (!FabricLoader.getInstance().isModLoaded("modmenu") || !ModMenuConfig.MODIFY_TITLE_SCREEN.getValue())
			cir.setReturnValue(cir.getReturnValue() - 24);
	}
}
