package dev.dannytaylor.dtaf2026.client.registry.gui;

import dev.dannytaylor.dtaf2026.common.registry.gui.ScreenHandlerRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class ClientScreenHandlerRegistry {
	public static void bootstrap() {
		HandledScreens.register(ScreenHandlerRegistry.extractor, ExtractorScreen::new);
	}
}
