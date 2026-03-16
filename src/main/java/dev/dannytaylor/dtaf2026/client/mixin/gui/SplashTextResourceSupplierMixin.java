/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.mixin.gui;

import dev.dannytaylor.dtaf2026.common.data.Data;
import net.minecraft.client.gui.screen.SplashTextRenderer;
import net.minecraft.client.resource.SplashTextResourceSupplier;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(SplashTextResourceSupplier.class)
public abstract class SplashTextResourceSupplierMixin {
	@Shadow
	@Final
	private static Random RANDOM;
	@Shadow
	@Final
	private List<String> splashTexts;

	@ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ResourceManager;openAsReader(Lnet/minecraft/util/Identifier;)Ljava/io/BufferedReader;"), method = "prepare(Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)Ljava/util/List;")
	private Identifier dtaf2026$modifyId(Identifier id) {
		return Data.idOf(id.getPath());
	}

	@Inject(method = "get", at = @At("HEAD"), cancellable = true)
	private void dtaf2026$get(CallbackInfoReturnable<SplashTextRenderer> cir) {
		cir.setReturnValue(this.splashTexts.isEmpty() ? null : new SplashTextRenderer(this.splashTexts.get(RANDOM.nextInt(this.splashTexts.size()))));
	}
}
