package dev.dannytaylor.dtaf2026.common.registry.gui;

import dev.dannytaylor.dtaf2026.common.data.Data;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ScreenHandlerRegistry {
	public static final ScreenHandlerType<ExtractorScreenHandler> extractor;

	public static void bootstrap() {
	}

	public static <T extends ScreenHandler> ScreenHandlerType<T> register(String path, ScreenHandlerType.Factory<T> factory) {
		return register(Data.idOf(path), factory);
	}

	public static <T extends ScreenHandler> ScreenHandlerType<T> register(Identifier identifier, ScreenHandlerType.Factory<T> factory) {
		return Registry.register(Registries.SCREEN_HANDLER, identifier, new ScreenHandlerType<>(factory, FeatureFlags.VANILLA_FEATURES));
	}

	static {
		extractor = register("extractor", ExtractorScreenHandler::new);
	}
}
