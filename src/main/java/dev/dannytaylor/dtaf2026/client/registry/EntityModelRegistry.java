/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry;

import dev.dannytaylor.dtaf2026.common.data.Data;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.model.BoatEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;

public class EntityModelRegistry {
	public static final EntityModelLayer mapleBoat;
	public static final EntityModelLayer mapleChestBoat;

	public static void bootstrap() {
		EntityModelLayerRegistry.registerModelLayer(mapleBoat, BoatEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(mapleChestBoat, BoatEntityModel::getChestTexturedModelData);
	}

	static {
		mapleBoat = new EntityModelLayer(Data.idOf("boat/maple"), "main");
		mapleChestBoat = new EntityModelLayer(Data.idOf("chest_boat/maple"), "main");
	}
}
