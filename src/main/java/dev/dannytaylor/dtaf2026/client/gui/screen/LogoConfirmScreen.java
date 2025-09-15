/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.gui.screen;

import dev.dannytaylor.dtaf2026.client.data.ClientData;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.LogoDrawer;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.SplashTextRenderer;
import net.minecraft.client.gui.widget.EmptyWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public class LogoConfirmScreen extends ConfirmScreen {
	private final LogoDrawer logoDrawer;
	private final SplashTextRenderer splashText;
	public LogoConfirmScreen(BooleanConsumer callback, Text title, Text message, Text yesText, Text noText) {
		super(callback, title, message, yesText, noText);
		this.logoDrawer = new LogoDrawer(true);
		this.splashText = new SplashTextRenderer("");
		this.layout.add(new EmptyWidget(64, 64));
	}
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		this.logoDrawer.draw(context, this.width, 1.0F);
		if (this.splashText != null && !(Boolean) ClientData.getMinecraft().options.getHideSplashTexts().getValue()) {
			this.splashText.render(context, this.width, this.textRenderer, MathHelper.ceil(255.0F) << 24);
		}
	}
}
