/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry.entity.render_state;

import dev.dannytaylor.dtaf2026.common.registry.entity.junglefowl.JunglefowlVariant;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;

@Environment(EnvType.CLIENT)
public class JunglefowlEntityRenderState extends LivingEntityRenderState {
	public float flapProgress;
	public float maxWingDeviation;
	public JunglefowlVariant variant;
}
