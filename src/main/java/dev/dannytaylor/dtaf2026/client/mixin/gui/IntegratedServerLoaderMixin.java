/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.mixin.gui;

import com.mojang.serialization.Lifecycle;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.server.integrated.IntegratedServerLoader;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IntegratedServerLoader.class)
public class IntegratedServerLoaderMixin {
	@Inject(method = "showBackupPromptScreen", at = @At("HEAD"), cancellable = true)
	private void dtaf2026$removeBackupPromptScreen(LevelStorage.Session session, boolean customized, Runnable callback, Runnable onCancel, CallbackInfo ci) {
		callback.run();
		ci.cancel();
	}
	@Inject(method = "tryLoad", at = @At("HEAD"), cancellable = true)
	private static void dtaf2026$tryLoad(MinecraftClient client, CreateWorldScreen parent, Lifecycle lifecycle, Runnable loader, boolean bypassWarnings, CallbackInfo ci) {
		loader.run();
		ci.cancel();
	}
}
