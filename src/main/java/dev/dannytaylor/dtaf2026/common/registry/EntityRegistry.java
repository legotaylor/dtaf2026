/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry;

import dev.dannytaylor.dtaf2026.common.data.Data;
import dev.dannytaylor.dtaf2026.common.registry.entity.boar.BoarEntity;
import dev.dannytaylor.dtaf2026.common.registry.entity.boar.BoarVariants;
import dev.dannytaylor.dtaf2026.common.registry.entity.fleecifer.FleeciferBossEntity;
import dev.dannytaylor.dtaf2026.common.registry.entity.fleecifer.FleeciferEntity;
import dev.dannytaylor.dtaf2026.common.registry.entity.fleecifer.FleeciferEyeEntity;
import dev.dannytaylor.dtaf2026.common.registry.entity.fleecifer.FleeciferFight;
import dev.dannytaylor.dtaf2026.common.registry.entity.junglefowl.JunglefowlEntity;
import dev.dannytaylor.dtaf2026.common.registry.entity.junglefowl.JunglefowlVariants;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.ChestBoatEntity;
import net.minecraft.entity.vehicle.ChestRaftEntity;
import net.minecraft.entity.vehicle.RaftEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.*;
import java.util.function.Supplier;

public class EntityRegistry {
	public static final EntityType<BoatEntity> mapleBoat;
	public static final EntityType<ChestBoatEntity> mapleChestBoat;
	public static final EntityType<BoatEntity> ceruleanBoat;
	public static final EntityType<ChestBoatEntity> ceruleanChestBoat;
	public static final EntityType<JunglefowlEntity> junglefowl;
	public static final EntityType<BoarEntity> boar;
	public static final EntityType<FleeciferBossEntity> fleeciferBoss;
	public static final EntityType<FleeciferEntity> fleecifer;
	public static final EntityType<FleeciferEyeEntity> fleeciferEye;

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

	private static final Map<UUID, FleeciferFight> fleeciferFights = new HashMap<>();

	public static FleeciferFight addFleeciferFight(FleeciferFight fight) {
		fleeciferFights.put(fight.getUuid(), fight);
		return fight;
	}

	public static Map<UUID, FleeciferFight> getFleeciferFights() {
		return fleeciferFights;
	}

	public static boolean hasFleeciferFight() {
		return !fleeciferFights.isEmpty();
	}

	public static void bootstrap() {
		FabricDefaultAttributeRegistry.register(junglefowl, JunglefowlEntity.createJunglefowlAttributes());
		FabricDefaultAttributeRegistry.register(boar, BoarEntity.createBoarAttributes());
		FabricDefaultAttributeRegistry.register(fleeciferBoss, FleeciferBossEntity.createFleeciferAttributes());
		FabricDefaultAttributeRegistry.register(fleecifer, FleeciferEntity.createFleeciferAttributes());
		JunglefowlVariants.bootstrap();
		BoarVariants.bootstrap();
		ServerTickEvents.END_WORLD_TICK.register((world) -> {
			if (hasFleeciferFight()) {
				List<UUID> forRemoval = new ArrayList<>();
				for (FleeciferFight fight : fleeciferFights.values()) {
					if (!fight.completed) fight.tick();
					else forRemoval.add(fight.getUuid());
				}
				for (UUID uuid : forRemoval) fleeciferFights.remove(uuid);
			}
		});
	}

	static {
		mapleBoat = register("maple_boat", getBoatBuilder(() -> ItemRegistry.maple.boat));
		mapleChestBoat = register("maple_chest_boat", getChestBoatBuilder(() -> ItemRegistry.maple.chestBoat));
		ceruleanBoat = register("cerulean_boat", getBoatBuilder(() -> ItemRegistry.cerulean.boat));
		ceruleanChestBoat = register("cerulean_chest_boat", getChestBoatBuilder(() -> ItemRegistry.cerulean.chestBoat));
		junglefowl = register("junglefowl", EntityType.Builder.create(JunglefowlEntity::new, SpawnGroup.CREATURE).dimensions(0.5F, 0.875F).eyeHeight(0.805F).passengerAttachments(new Vec3d(0.0F, 0.875, -0.125)).maxTrackingRange(10));
		boar = register("boar", EntityType.Builder.create(BoarEntity::new, SpawnGroup.CREATURE).dimensions(0.9F, 0.9F).passengerAttachments(0.86875F).maxTrackingRange(10));
		fleeciferBoss = register("fleecifer_boss", EntityType.Builder.create(FleeciferBossEntity::new, SpawnGroup.MONSTER).dimensions(0.9F, 1.3F).eyeHeight(1.235F).passengerAttachments(1.2375F).maxTrackingRange(40));
		fleecifer = register("fleecifer", EntityType.Builder.create(FleeciferEntity::new, SpawnGroup.MONSTER).dimensions(0.9F, 1.3F).eyeHeight(1.235F).passengerAttachments(1.2375F).maxTrackingRange(40));
		fleeciferEye = register("fleecifer_eye", EntityType.Builder.create(FleeciferEyeEntity::new, SpawnGroup.MISC).dropsNothing().dimensions(0.25F, 0.25F).maxTrackingRange(4).trackingTickInterval(4));
	}
}
