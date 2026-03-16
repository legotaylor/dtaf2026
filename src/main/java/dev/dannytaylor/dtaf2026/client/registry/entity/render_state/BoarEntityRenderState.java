/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry.entity.render_state;

import dev.dannytaylor.dtaf2026.common.registry.entity.boar.BoarVariant;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.item.ItemStack;

@Environment(EnvType.CLIENT)
public class BoarEntityRenderState extends LivingEntityRenderState {
	public ItemStack saddleStack;
	public BoarVariant variant;

	public BoarEntityRenderState() {
		this.saddleStack = ItemStack.EMPTY;
	}
}
