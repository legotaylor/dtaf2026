/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.mixin.entity;

import dev.dannytaylor.dtaf2026.client.registry.entity.render_state.CreakingVariantEntityRenderState;
import net.minecraft.client.render.entity.state.CreakingEntityRenderState;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(CreakingEntityRenderState.class)
public abstract class CreakingEntityRenderStateMixin implements CreakingVariantEntityRenderState {
	@Unique
	private Identifier variant;

	public Identifier dtaf2026$getVariant() {
		return this.variant;
	}

	public void dtaf2026$setVariant(Identifier variant) {
		this.variant = variant;
	}
}
