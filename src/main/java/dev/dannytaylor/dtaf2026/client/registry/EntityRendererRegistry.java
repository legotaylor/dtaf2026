/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry;

import dev.dannytaylor.dtaf2026.common.registry.EntityRegistry;
import net.minecraft.client.render.entity.BoatEntityRenderer;

public class EntityRendererRegistry {
	public static void bootstrap() {
		net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register(EntityRegistry.mapleBoat, (context) -> new BoatEntityRenderer(context, EntityModelRegistry.mapleBoat));
		net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register(EntityRegistry.mapleChestBoat, (context) -> new BoatEntityRenderer(context, EntityModelRegistry.mapleChestBoat));
	}
}
