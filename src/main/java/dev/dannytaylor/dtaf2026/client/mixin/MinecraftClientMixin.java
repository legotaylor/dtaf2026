/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.mixin;

import dev.dannytaylor.dtaf2026.client.data.ClientData;
import dev.dannytaylor.dtaf2026.client.gui.ScreenHelper;
import dev.dannytaylor.dtaf2026.client.registry.entity.ClientVariantRegistries;
import dev.dannytaylor.dtaf2026.common.registry.worldgen.DimensionRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.MusicInstance;
import net.minecraft.sound.MusicType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Function;

@Mixin(value = MinecraftClient.class, priority = 100)
public abstract class MinecraftClientMixin {
	@Shadow
	@Final
	public InGameHud inGameHud;
	@Shadow
	@Nullable
	public ClientPlayerEntity player;

	@Inject(at = @At("RETURN"), method = "createInitScreens", cancellable = true)
	private void dtaf2026$createInitScreens(List<Function<Runnable, Screen>> list, CallbackInfoReturnable<Boolean> cir) {
		if (ScreenHelper.showFlashingLightsWarning(list)) cir.setReturnValue(true);
	}

	@Inject(method = "onDisconnected", at = @At("HEAD"))
	private void dtaf2026$onDisconnected(CallbackInfo ci) {
		ClientVariantRegistries.instance.resetAll();
	}

	@Inject(method = "getMusicInstance", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBiome(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/registry/entry/RegistryEntry;"), cancellable = true)
	private void dtaf2026$getMusicInstance(CallbackInfoReturnable<MusicInstance> cir) {
		if (this.player != null && this.player.getWorld().getRegistryKey() == DimensionRegistry.theTerrorlands.world() && this.inGameHud.getBossBarHud().shouldPlayDragonMusic()) {
			// It would be cool to have custom music for the boss fight, but I only have two weeks left-
			cir.setReturnValue(new MusicInstance(MusicType.DRAGON));
		}
	}

	@Inject(method = "setScreen", at = @At("HEAD"))
	private void dtaf2026$setScreen(Screen screen, CallbackInfo ci) {
		if (screen instanceof TitleScreen titleScreen)
			titleScreen.splashText = ClientData.getMinecraft().getSplashTextLoader().get();
	}
}
