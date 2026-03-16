/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry.entity;

import dev.dannytaylor.dtaf2026.client.registry.entity.renderer.BoarEntityRenderer;
import dev.dannytaylor.dtaf2026.client.registry.entity.renderer.FleeciferBossEntityRenderer;
import dev.dannytaylor.dtaf2026.client.registry.entity.renderer.FleeciferEntityRenderer;
import dev.dannytaylor.dtaf2026.client.registry.entity.renderer.JunglefowlEntityRenderer;
import dev.dannytaylor.dtaf2026.common.registry.entity.EntityRegistry;
import net.minecraft.client.render.entity.BoatEntityRenderer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

public class EntityRendererRegistry {
	public static void bootstrap() {
		net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register(EntityRegistry.mapleBoat, (context) -> new BoatEntityRenderer(context, EntityModelRegistry.mapleBoat));
		net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register(EntityRegistry.mapleChestBoat, (context) -> new BoatEntityRenderer(context, EntityModelRegistry.mapleChestBoat));
		net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register(EntityRegistry.ceruleanBoat, (context) -> new BoatEntityRenderer(context, EntityModelRegistry.ceruleanBoat));
		net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register(EntityRegistry.ceruleanChestBoat, (context) -> new BoatEntityRenderer(context, EntityModelRegistry.ceruleanChestBoat));
		net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register(EntityRegistry.boar, BoarEntityRenderer::new);
		net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register(EntityRegistry.junglefowl, JunglefowlEntityRenderer::new);
		net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register(EntityRegistry.fleeciferBoss, FleeciferBossEntityRenderer::new);
		net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register(EntityRegistry.fleecifer, FleeciferEntityRenderer::new);
		net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register(EntityRegistry.fleeciferEye, (context) -> new FlyingItemEntityRenderer<>(context, 1.0F, true));
	}
}
