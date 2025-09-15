/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.mixin.gui;

import dev.dannytaylor.dtaf2026.client.gui.SomniumRealeWorldEntryReason;
import dev.dannytaylor.dtaf2026.common.registry.DimensionRegistry;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
	@Inject(method = "getWorldEntryReason", at = @At("RETURN"), cancellable = true)
	private void dtaf2026$getWorldEntryReason(boolean dead, RegistryKey<World> from, RegistryKey<World> _to, CallbackInfoReturnable<DownloadingTerrainScreen.WorldEntryReason> cir) {
		if (_to.equals(DimensionRegistry.somniumReale.world()) || from.equals(DimensionRegistry.somniumReale.world())) {
			cir.setReturnValue(SomniumRealeWorldEntryReason.getCustomWorldEntryReasons().get("SOMNIUM_REALE"));
		}
	}
}
