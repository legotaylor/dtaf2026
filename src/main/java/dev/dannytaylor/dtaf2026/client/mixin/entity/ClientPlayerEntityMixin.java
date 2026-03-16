/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.mixin.entity;

import com.mojang.authlib.GameProfile;
import dev.dannytaylor.dtaf2026.client.contributor.Contributor;
import dev.dannytaylor.dtaf2026.client.contributor.ContributorPlayerEntity;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity implements ContributorPlayerEntity {
	@Unique
	private boolean isBlinking;
	@Unique
	private int prevBlink;

	public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
		super(world, profile);
	}

	@Override
	public boolean dtaf2026$getBlinking() {
		return this.isBlinking;
	}

	@Override
	public void dtaf2026$setBlinking(boolean blinking) {
		this.isBlinking = blinking;
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void dtaf2026$tick(CallbackInfo ci) {
		String uuid = this.getUuid().toString();
		if (Contributor.isContributor(uuid)) {
			if (!dtaf2026$getBlinking()) {
				if (this.prevBlink > 40) {
					if (this.prevBlink > 200 || this.getRandom().nextBoolean()) {
						this.dtaf2026$setBlinking(true);
						this.prevBlink = 0;
					}
				}
			} else {
				if (this.prevBlink > 2) {
					if (this.prevBlink > 6 || this.getRandom().nextBoolean()) {
						this.dtaf2026$setBlinking(false);
						this.prevBlink = 0;
					}
				}
			}
			this.prevBlink++;
		}
	}
}
