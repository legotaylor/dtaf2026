/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry;

import dev.dannytaylor.dtaf2026.common.data.Data;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.vehicle.*;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public class EntityRegistry {
	public static final EntityType<BoatEntity> mapleBoat;
	public static final EntityType<ChestBoatEntity> mapleChestBoat;

	public static <T extends Entity> EntityType<T> register(Identifier id, EntityType.Builder<T> type) {
		RegistryKey<EntityType<?>> key = RegistryKey.of(RegistryKeys.ENTITY_TYPE, id);
		return Registry.register(Registries.ENTITY_TYPE, key, type.build(key));
	}

	public static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> type) {
		return register(Data.idOf(id), type);
	}

	public static EntityType.Builder<BoatEntity> getBoatBuilder(Supplier<Item> dropsItem) {
		return EntityType.Builder.create(getBoatFactory(dropsItem), SpawnGroup.MISC).dropsNothing().dimensions(1.375F, 0.5625F).eyeHeight(0.5625F).maxTrackingRange(10);
	}

	public static EntityType.Builder<ChestBoatEntity> getChestBoatBuilder(Supplier<Item> dropsItem) {
		return EntityType.Builder.create(getChestBoatFactory(dropsItem), SpawnGroup.MISC).dropsNothing().dimensions(1.375F, 0.5625F).eyeHeight(0.5625F).maxTrackingRange(10);
	}

	public static EntityType.Builder<RaftEntity> getRaftBuilder(Supplier<Item> dropsItem) {
		return EntityType.Builder.create(getRaftFactory(dropsItem), SpawnGroup.MISC).dropsNothing().dimensions(1.375F, 0.5625F).eyeHeight(0.5625F).maxTrackingRange(10);
	}

	public static EntityType.Builder<ChestRaftEntity> getChestRaftBuilder(Supplier<Item> dropsItem) {
		return EntityType.Builder.create(getChestRaftFactory(dropsItem), SpawnGroup.MISC).dropsNothing().dimensions(1.375F, 0.5625F).eyeHeight(0.5625F).maxTrackingRange(10);
	}

	public static EntityType.EntityFactory<BoatEntity> getBoatFactory(Supplier<Item> dropsItem) {
		return (type, world) -> new BoatEntity(type, world, dropsItem);
	}

	public static EntityType.EntityFactory<ChestBoatEntity> getChestBoatFactory(Supplier<Item> dropsItem) {
		return (type, world) -> new ChestBoatEntity(type, world, dropsItem);
	}

	public static EntityType.EntityFactory<RaftEntity> getRaftFactory(Supplier<Item> dropsItem) {
		return (type, world) -> new RaftEntity(type, world, dropsItem);
	}

	public static EntityType.EntityFactory<ChestRaftEntity> getChestRaftFactory(Supplier<Item> dropsItem) {
		return (type, world) -> new ChestRaftEntity(type, world, dropsItem);
	}

	public static void bootstrap() {
	}

	static {
		mapleBoat = register("maple_boat", getBoatBuilder(() -> ItemRegistry.maple.boat));
		mapleChestBoat = register("maple_chest_boat", getChestBoatBuilder(() -> ItemRegistry.maple.chestBoat));
	}
}
