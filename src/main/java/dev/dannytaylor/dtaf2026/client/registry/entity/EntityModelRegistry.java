/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry.entity;

import dev.dannytaylor.dtaf2026.client.contributor.ContributorFaceModel;
import dev.dannytaylor.dtaf2026.client.registry.entity.model.*;
import dev.dannytaylor.dtaf2026.common.data.Data;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.render.entity.model.BoatEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;

public class EntityModelRegistry {
	public static final EntityModelLayer mapleBoat;
	public static final EntityModelLayer mapleChestBoat;
	public static final EntityModelLayer ceruleanBoat;
	public static final EntityModelLayer ceruleanChestBoat;
	public static final EntityModelLayer boar;
	public static final EntityModelLayer babyBoar;
	public static final EntityModelLayer bigJunglefowl;
	public static final EntityModelLayer babyBigJunglefowl;
	public static final EntityModelLayer smallJunglefowl;
	public static final EntityModelLayer babySmallJunglefowl;
	public static final EntityModelLayer fleecifer;
	public static final EntityModelLayer fleeciferWool;
	public static final EntityModelLayer fleeciferWoolUndercoat;
	public static final EntityModelLayer contributorFace;

	static {
		mapleBoat = new EntityModelLayer(Data.idOf("boat/maple"), "main");
		mapleChestBoat = new EntityModelLayer(Data.idOf("chest_boat/maple"), "main");
		ceruleanBoat = new EntityModelLayer(Data.idOf("boat/cerulean"), "main");
		ceruleanChestBoat = new EntityModelLayer(Data.idOf("chest_boat/cerulean"), "main");
		boar = new EntityModelLayer(Data.idOf("boar"), "main");
		babyBoar = new EntityModelLayer(Data.idOf("boar"), "baby");
		bigJunglefowl = new EntityModelLayer(Data.idOf("junglefowl/big"), "main");
		babyBigJunglefowl = new EntityModelLayer(Data.idOf("junglefowl/big"), "baby");
		smallJunglefowl = new EntityModelLayer(Data.idOf("junglefowl/small"), "main");
		babySmallJunglefowl = new EntityModelLayer(Data.idOf("junglefowl/small"), "baby");
		fleecifer = new EntityModelLayer(Data.idOf("fleecifer"), "main");
		fleeciferWool = new EntityModelLayer(Data.idOf("fleecifer"), "wool");
		fleeciferWoolUndercoat = new EntityModelLayer(Data.idOf("fleecifer"), "wool_undercoat");
		contributorFace = new EntityModelLayer(Data.idOf("contributor"), "face");
	}

	public static void bootstrap() {
		EntityModelLayerRegistry.registerModelLayer(mapleBoat, BoatEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(mapleChestBoat, BoatEntityModel::getChestTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(ceruleanBoat, BoatEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(ceruleanChestBoat, BoatEntityModel::getChestTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(boar, BoarEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(babyBoar, () -> BoarEntityModel.getTexturedModelData().transform(BoarEntityModel.babyTransformer));
		EntityModelLayerRegistry.registerModelLayer(bigJunglefowl, BigJunglefowlEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(babyBigJunglefowl, () -> BigJunglefowlEntityModel.getTexturedModelData().transform(AbstractJunglefowlEntityModel.babyTransformer));
		EntityModelLayerRegistry.registerModelLayer(smallJunglefowl, SmallJunglefowlEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(babySmallJunglefowl, () -> SmallJunglefowlEntityModel.getTexturedModelData().transform(AbstractJunglefowlEntityModel.babyTransformer));
		EntityModelLayerRegistry.registerModelLayer(fleecifer, FleeciferEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(fleeciferWool, FleeciferWoolEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(fleeciferWoolUndercoat, FleeciferWoolEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(contributorFace, () -> ContributorFaceModel.getTexturedModelData(Dilation.NONE));
	}
}
