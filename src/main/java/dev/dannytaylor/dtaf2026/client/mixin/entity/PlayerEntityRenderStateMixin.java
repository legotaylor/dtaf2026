/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.mixin.entity;

import dev.dannytaylor.dtaf2026.client.contributor.ContributorPlayerEntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PlayerEntityRenderState.class)
public abstract class PlayerEntityRenderStateMixin implements ContributorPlayerEntityRenderState {
	@Unique
	private boolean isBlinking;

	@Override
	public boolean dtaf2026$getBlinking() {
		return this.isBlinking;
	}

	@Override
	public void dtaf2026$setBlinking(boolean blinking) {
		this.isBlinking = blinking;
	}
}
