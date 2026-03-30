package dev.dannytaylor.dtaf2026.client.gui;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class TextListTooltipComponent implements TooltipComponent {
	private final List<OrderedText> texts;

	public TextListTooltipComponent(MutableText... texts) {
		List<OrderedText> listedTexts = new ArrayList<>();
		for (MutableText text : texts) {
			for (String line : text.getString().split("\n", -1)) {
				listedTexts.add(Text.literal(line).styled(style -> style.withParent(text.getStyle())).asOrderedText());
			}
		}
		this.texts = listedTexts;
	}

	public int getWidth(TextRenderer textRenderer) {
		int width = 0;
		for (OrderedText text : this.texts) width = Math.max(width, textRenderer.getWidth(text));
		return width;
	}

	public int getHeight(TextRenderer textRenderer) {
		return 10 * this.texts.size();
	}

	public void drawText(DrawContext context, TextRenderer textRenderer, int x, int y) {
		for (int i = 0; i < this.texts.size(); i++)
			context.drawText(textRenderer, this.texts.get(i), x, y + (10 * i), -1, true);
	}
}
