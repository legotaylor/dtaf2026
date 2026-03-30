package dev.dannytaylor.dtaf2026.common.registry.item;

import com.google.common.collect.Maps;
import dev.dannytaylor.dtaf2026.common.data.Data;
import dev.dannytaylor.dtaf2026.common.registry.tagkey.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.equipment.*;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

import java.util.Map;

public class MaterialRegistry {
	public static class Armor {
		public static final ArmorMaterial jasper;

		private static ArmorMaterial of(RegistryEntry<SoundEvent> equipSound, TagKey<Item> repairItems, String path) {
			return new ArmorMaterial(35, createDefenseMap(3, 6, 8, 3, 11), 25, equipSound, 2.0F, 0.0F, repairItems, registerEquipmentAssetKey(path));
		}

		public static Map<EquipmentType, Integer> createDefenseMap(int bootsDefense, int leggingsDefense, int chestplateDefense, int helmetDefense, int bodyDefense) {
			return Maps.newEnumMap(Map.of(EquipmentType.BOOTS, bootsDefense, EquipmentType.LEGGINGS, leggingsDefense, EquipmentType.CHESTPLATE, chestplateDefense, EquipmentType.HELMET, helmetDefense, EquipmentType.BODY, bodyDefense));
		}

		public static RegistryKey<EquipmentAsset> registerEquipmentAssetKey(String path) {
			return registerEquipmentAssetKey(Data.idOf(path));
		}

		public static RegistryKey<EquipmentAsset> registerEquipmentAssetKey(Identifier identifier) {
			return RegistryKey.of(EquipmentAssetKeys.REGISTRY_KEY, identifier);
		}

		static {
			jasper = of(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, TagRegistry.Item.repairsAmberArmor, "jasper");
		}
	}

	public static class Tool {
		public static final ToolMaterial ruby;
		public static final ToolMaterial quarite;

		private static ToolMaterial of(TagKey<Block> incorrectForTool, TagKey<Item> toolMaterials) {
			return new ToolMaterial(incorrectForTool, 1796, 12.0F, 4.0F, 22, toolMaterials);
		}

		static {
			ruby = of(TagRegistry.Block.incorrectForRubyTool, TagRegistry.Item.rubyToolMaterials);
			quarite = of(TagRegistry.Block.incorrectForQuariteTool, TagRegistry.Item.quariteToolMaterials);
		}
	}

	public static void bootstrap() {
	}
}
