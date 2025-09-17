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
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class LogoConfirmScreen extends ConfirmScreen {
	public final LogoDrawer logoDrawer;
	public final int disableTicks;
	@Nullable public CheckboxWidget checkBox;
	public NarratedMultilineTextWidget narriatorTextWidget;
	public final Runnable onCheckBoxProceed;
	public final Text checkBoxText;
	public final Text message;
	public final boolean narriatorMessage;
	public final boolean renderTitle;
	public final boolean renderLogo;

	public LogoConfirmScreen(BooleanConsumer buttonCallback, Text title, Text message, Text yesText, Text noText) {
		this(buttonCallback, null, title, message, true, yesText, noText, Text.empty(), 0, 80, false, true);
	}

	public LogoConfirmScreen(BooleanConsumer buttonCallback, @Nullable Runnable onCheckBoxProceed, Text title, Text message, Text yesText, Text noText, Text checkBoxText) {
		this(buttonCallback, onCheckBoxProceed, title, message, true, yesText, noText, checkBoxText, 60, 80, false);
	}

	public LogoConfirmScreen(BooleanConsumer buttonCallback, @Nullable Runnable onCheckBoxProceed, Text title, Text message, boolean narriatorMessage, Text yesText, Text noText, Text checkBoxText, int buttonEnableTimer, int topYOffset, boolean renderTitle) {
		this(buttonCallback, onCheckBoxProceed, title, message, narriatorMessage, yesText, noText, checkBoxText, buttonEnableTimer, topYOffset, renderTitle, true);
	}

	public LogoConfirmScreen(BooleanConsumer buttonCallback, @Nullable Runnable onCheckBoxProceed, Text title, Text message, boolean narriatorMessage, Text yesText, Text noText, Text checkBoxText, int buttonEnableTimer, int topYOffset, boolean renderTitle, boolean renderLogo) {
		super(buttonCallback, title, message, yesText, noText);
		this.logoDrawer = new LogoDrawer(true);
		this.disableTicks = buttonEnableTimer;
		this.onCheckBoxProceed = onCheckBoxProceed;
		this.checkBoxText = checkBoxText;
		this.message = message;
		this.narriatorMessage = narriatorMessage;
		this.renderTitle = renderTitle;
		this.renderLogo = renderLogo;
		this.layout.add(new EmptyWidget(topYOffset, topYOffset));
	}

	protected void init() {
		this.layout.getMainPositioner().alignHorizontalCenter();
		if (this.renderTitle) this.layout.add(new TextWidget(this.title, this.textRenderer));
		if (this.narriatorMessage) {
			this.narriatorTextWidget = this.layout.add(new NarratedMultilineTextWidget(this.width - 100, this.message, this.textRenderer, 12), (positioner) -> positioner.margin(12));
			this.narriatorTextWidget.setCentered(false);
		} else {
			this.layout.add((new MultilineTextWidget(this.message, this.textRenderer)).setMaxWidth(this.width - 50).setMaxRows(15).setCentered(true));
		}
		this.initExtras();
		DirectionalLayoutWidget directionalLayoutWidget = this.layout.add(DirectionalLayoutWidget.horizontal().spacing(4));
		directionalLayoutWidget.getMainPositioner().marginTop(16);
		this.addButtons(directionalLayoutWidget);
		this.layout.forEachChild(this::addDrawableChild);
		this.refreshWidgetPositions();
	}

	protected void refreshWidgetPositions() {
		if (this.narriatorMessage && this.narriatorTextWidget != null) {
			this.narriatorTextWidget.setMaxWidth(this.width - 100);
		}
		super.refreshWidgetPositions();
	}

	protected void initExtras() {
		this.checkBox = this.layout.add(CheckboxWidget.builder(this.checkBoxText, ClientData.getMinecraft().textRenderer).build());
		DirectionalLayoutWidget buttonLayout = this.layout.add(DirectionalLayoutWidget.horizontal().spacing(4));
		this.yesButton = buttonLayout.add(ButtonWidget.builder(this.yesText, (button) -> {
			if (this.checkBox != null && this.checkBox.isChecked()) this.onCheckBoxProceed.run();
			this.callback.accept(true);
		}).build());
		this.noButton = buttonLayout.add(ButtonWidget.builder(this.noText, (button) -> this.callback.accept(false)).build());
		if (this.disableTicks > 0) this.disableButtons(this.disableTicks);
	}

	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		if (this.renderLogo) this.logoDrawer.draw(context, this.width, 1.0F);
	}

	protected void addButtons(DirectionalLayoutWidget layout) {
	}
}
