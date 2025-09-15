/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.mixin.gui;

import dev.dannytaylor.dtaf2026.client.data.ClientData;
import dev.dannytaylor.dtaf2026.client.gui.SomniumRealeWorldEntryReason;
import dev.dannytaylor.dtaf2026.common.registry.BlockRegistry;
import dev.dannytaylor.dtaf2026.common.utils.UnsafeEnumHelper;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.texture.Sprite;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.Map;

@Mixin(value = DownloadingTerrainScreen.class)
public abstract class DownloadingTerrainScreenMixin {
	@Shadow @Final private DownloadingTerrainScreen.WorldEntryReason worldEntryReason;

	@Mixin(value = DownloadingTerrainScreen.WorldEntryReason.class, priority = 100)
	public static class WorldEntryReasonMixin {
		@Inject(method = "<clinit>", at = @At("RETURN"))
		private static void dtaf2026$addCustomProgram(CallbackInfo ci) {
			SomniumRealeWorldEntryReason.bootstrap();
			SomniumRealeWorldEntryReason.getWorldEntryReasons().forEach((id) -> {
				try {
					System.out.println("Registering custom world entry reason with id: " + id);
					DownloadingTerrainScreen.WorldEntryReason[] oldValues = DownloadingTerrainScreen.WorldEntryReason.values();
					DownloadingTerrainScreen.WorldEntryReason customReason = UnsafeEnumHelper.createEnum(DownloadingTerrainScreen.WorldEntryReason.class, id, oldValues.length, Map.of());
					SomniumRealeWorldEntryReason.registerCustom(id, customReason);
					DownloadingTerrainScreen.WorldEntryReason[] newValues = Arrays.copyOf(oldValues, oldValues.length + 1);
					newValues[newValues.length - 1] = customReason;
					UnsafeEnumHelper.UNSAFE.putObject(DownloadingTerrainScreen.WorldEntryReason.class, UnsafeEnumHelper.UNSAFE.staticFieldOffset(Arrays.stream(DownloadingTerrainScreen.WorldEntryReason.class.getDeclaredFields()).filter(f -> f.getType().equals(DownloadingTerrainScreen.WorldEntryReason[].class)).findFirst().orElseThrow()), newValues);
				} catch (Exception error) {
					error.printStackTrace();
				}
			});
		}
	}

	@Inject(method = "renderBackground", at = @At("HEAD"), cancellable = true)
	private void dtaf2026$renderBackground(DrawContext context, int mouseX, int mouseY, float deltaTicks, CallbackInfo ci) {
		if (this.worldEntryReason.equals(SomniumRealeWorldEntryReason.getCustomWorldEntryReasons().get("SOMNIUM_REALE"))) {
			context.drawSpriteStretched(RenderPipelines.GUI_OPAQUE_TEX_BG, this.getSomniumRealeBackgroundSprite(), 0, 0, context.getScaledWindowWidth(), context.getScaledWindowHeight());
			ci.cancel();
		}
	}

	@Unique private Sprite somniumRealeBackgroundSprite;

	@Unique
	private Sprite getSomniumRealeBackgroundSprite() {
		if (this.somniumRealeBackgroundSprite == null) this.somniumRealeBackgroundSprite = ClientData.getMinecraft().getBlockRenderManager().getModels().getModelParticleSprite(BlockRegistry.maple_wood.getDefaultState());
		return this.somniumRealeBackgroundSprite;
	}
}
