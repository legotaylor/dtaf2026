/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.item;

import dev.dannytaylor.dtaf2026.common.registry.block.BlockRegistry;
import dev.dannytaylor.dtaf2026.common.registry.block.SupportedWoodBlockSet;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.AbstractBoatEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BoatItem;
import net.minecraft.item.Item;
import net.minecraft.util.Rarity;

import java.util.ArrayList;
import java.util.List;

public class SupportedWoodItemSet {
	public final Blocks blocks;
	public final Blocks creativeBlocks;
	public final Item bark;
	public final Item sapling;
	public final Item boat;
	public final Item chestBoat;

	public List<Item> asList(boolean creative) {
		List<Item> list = new ArrayList<>();
		if (creative) list.addAll(this.creativeBlocks.asList());
		else {
			list.addAll(this.blocks.asList());
			list.add(this.bark);
			list.add(this.sapling);
			list.add(this.boat);
			list.add(this.chestBoat);
		}
		return list;
	}

	private SupportedWoodItemSet(Blocks blocks, Blocks creativeBlocks, Item bark, Item sapling, Item boat, Item chestBoat) {
		this.blocks = blocks;
		this.creativeBlocks = creativeBlocks;
		this.bark = bark;
		this.sapling = sapling;
		this.boat = boat;
		this.chestBoat = chestBoat;
	}

	public static Builder builder(SupportedWoodBlockSet blockSet) {
		return new Builder(blockSet);
	}

	public static class Blocks {
		public final WoodLog log;
		public final WoodLog wood;
		public final Item planks;
		public final Item stairs;
		public final Item slab;
		public final Item fence;
		public final Item fenceGate;
		public final Item door;
		public final Item trapdoor;
		public final Item pressurePlate;
		public final Item button;
		public final Item leaves;

		public List<Item> asList() {
			List<Item> list = new ArrayList<>();
			list.add(this.log.zero);
			list.add(this.log.one);
			list.add(this.log.two);
			list.add(this.wood.zero);
			list.add(this.wood.one);
			list.add(this.wood.two);
			list.add(this.planks);
			list.add(this.stairs);
			list.add(this.slab);
			list.add(this.fence);
			list.add(this.fenceGate);
			if (this.door != null) list.add(this.door);
			if (this.trapdoor != null) list.add(this.trapdoor);
			if (this.pressurePlate != null) list.add(this.pressurePlate);
			if (this.button != null) list.add(this.button);
			list.add(this.leaves);
			return list;
		}

		private Blocks(WoodLog log, WoodLog wood, Item planks, Item stairs, Item slab, Item fence, Item fenceGate, Item door, Item trapdoor, Item pressurePlate, Item button, Item leaves) {
			this.log = log;
			this.wood = wood;
			this.planks = planks;
			this.stairs = stairs;
			this.slab = slab;
			this.fence = fence;
			this.fenceGate = fenceGate;
			this.door = door;
			this.trapdoor = trapdoor;
			this.pressurePlate = pressurePlate;
			this.button = button;
			this.leaves = leaves;
		}

		public static class WoodLog {
			public final Item zero;
			public final Item one;
			public final Item two;

			private WoodLog(Item zero, Item one, Item two) {
				this.zero = zero;
				this.one = one;
				this.two = two;
			}
		}
	}

	public static class Builder {
		private final SupportedWoodBlockSet blockSet;

		public Builder(SupportedWoodBlockSet blockSet) {
			this.blockSet = blockSet;
		}

		private Blocks buildBlocks(boolean survival) {
			ItemRegistry.AutoItemGroup itemGroup = survival ? ItemRegistry.AutoItemGroup.survival : ItemRegistry.AutoItemGroup.creative;
			Item log = ItemRegistry.register(this.blockSet.id.withPrefixedPath(BlockRegistry.getLogIdPrefix(0, !survival)).withSuffixedPath("_log"), (settings) -> new SupportedLogBlockItem(this.blockSet.log, 0, survival, settings), new BlockItem.Settings().useBlockPrefixedTranslationKey().rarity(!survival ? Rarity.EPIC : Rarity.COMMON), itemGroup);
			Item stripped_log = ItemRegistry.register(this.blockSet.id.withPrefixedPath(BlockRegistry.getLogIdPrefix(1, !survival)).withSuffixedPath("_log"), (settings) -> new SupportedLogBlockItem(this.blockSet.log, 1, survival, settings), new BlockItem.Settings().useBlockPrefixedTranslationKey().rarity(!survival ? Rarity.EPIC : Rarity.COMMON), itemGroup);
			Item twice_stripped_log = ItemRegistry.register(this.blockSet.id.withPrefixedPath(BlockRegistry.getLogIdPrefix(2, !survival)).withSuffixedPath("_log"), (settings) -> new SupportedLogBlockItem(this.blockSet.log, 2, survival, settings), new BlockItem.Settings().useBlockPrefixedTranslationKey().rarity(!survival ? Rarity.EPIC : Rarity.COMMON), itemGroup);

			Item wood = ItemRegistry.register(this.blockSet.id.withPrefixedPath(BlockRegistry.getLogIdPrefix(0, !survival)).withSuffixedPath("_wood"), (settings) -> new SupportedLogBlockItem(this.blockSet.wood, 0, survival, settings), new BlockItem.Settings().useBlockPrefixedTranslationKey().rarity(!survival ? Rarity.EPIC : Rarity.COMMON), itemGroup);
			Item stripped_wood = ItemRegistry.register(this.blockSet.id.withPrefixedPath(BlockRegistry.getLogIdPrefix(1, !survival)).withSuffixedPath("_wood"), (settings) -> new SupportedLogBlockItem(this.blockSet.wood, 1, survival, settings), new BlockItem.Settings().useBlockPrefixedTranslationKey().rarity(!survival ? Rarity.EPIC : Rarity.COMMON), itemGroup);
			Item twice_stripped_wood = ItemRegistry.register(this.blockSet.id.withPrefixedPath(BlockRegistry.getLogIdPrefix(2, !survival)).withSuffixedPath("_wood"), (settings) -> new SupportedLogBlockItem(this.blockSet.wood, 2, survival, settings), new BlockItem.Settings().useBlockPrefixedTranslationKey().rarity(!survival ? Rarity.EPIC : Rarity.COMMON), itemGroup);

			Item planks = ItemRegistry.register(this.blockSet.id.withPrefixedPath(BlockRegistry.getIdPrefix(!survival)).withSuffixedPath("_planks"), (settings) -> new SupportedBlockItem(this.blockSet.planks, survival, settings), new BlockItem.Settings().useBlockPrefixedTranslationKey().rarity(!survival ? Rarity.EPIC : Rarity.COMMON), itemGroup);
			Item stairs = ItemRegistry.register(this.blockSet.id.withPrefixedPath(BlockRegistry.getIdPrefix(!survival)).withSuffixedPath("_stairs"), (settings) -> new SupportedBlockItem(this.blockSet.stairs, survival, settings), new BlockItem.Settings().useBlockPrefixedTranslationKey().rarity(!survival ? Rarity.EPIC : Rarity.COMMON), itemGroup);
			Item slab = ItemRegistry.register(this.blockSet.id.withPrefixedPath(BlockRegistry.getIdPrefix(!survival)).withSuffixedPath("_slab"), (settings) -> new SupportedBlockItem(this.blockSet.slab, survival, settings), new BlockItem.Settings().useBlockPrefixedTranslationKey().rarity(!survival ? Rarity.EPIC : Rarity.COMMON), itemGroup);
			Item fence = ItemRegistry.register(this.blockSet.id.withPrefixedPath(BlockRegistry.getIdPrefix(!survival)).withSuffixedPath("_fence"), (settings) -> new SupportedBlockItem(this.blockSet.fence, survival, settings), new BlockItem.Settings().useBlockPrefixedTranslationKey().rarity(!survival ? Rarity.EPIC : Rarity.COMMON), itemGroup);
			Item fenceGate = ItemRegistry.register(this.blockSet.id.withPrefixedPath(BlockRegistry.getIdPrefix(!survival)).withSuffixedPath("_fence_gate"), (settings) -> new SupportedBlockItem(this.blockSet.fenceGate, survival, settings), new BlockItem.Settings().useBlockPrefixedTranslationKey().rarity(!survival ? Rarity.EPIC : Rarity.COMMON), itemGroup);

			Item door = null;
			Item trapdoor = null;
			Item pressurePlate = null;
			Item button = null;
			if (survival) {
				door = ItemRegistry.register(this.blockSet.id.withSuffixedPath("_door"), (settings) -> new BlockItem(this.blockSet.door, settings), new BlockItem.Settings().useBlockPrefixedTranslationKey(), itemGroup);
				trapdoor = ItemRegistry.register(this.blockSet.id.withSuffixedPath("_trapdoor"), (settings) -> new BlockItem(this.blockSet.trapdoor, settings), new BlockItem.Settings().useBlockPrefixedTranslationKey(), itemGroup);
				pressurePlate = ItemRegistry.register(this.blockSet.id.withSuffixedPath("_pressure_plate"), (settings) -> new BlockItem(this.blockSet.pressurePlate, settings), new BlockItem.Settings().useBlockPrefixedTranslationKey(), itemGroup);
				button = ItemRegistry.register(this.blockSet.id.withSuffixedPath("_button"), (settings) -> new BlockItem(this.blockSet.button, settings), new BlockItem.Settings().useBlockPrefixedTranslationKey(), itemGroup);
			}

			Item leaves = ItemRegistry.register(this.blockSet.id.withPrefixedPath(BlockRegistry.getIdPrefix(!survival)).withSuffixedPath("_leaves"), (settings) -> new SupportedBlockItem(this.blockSet.leaves, survival, settings), new BlockItem.Settings().useBlockPrefixedTranslationKey().rarity(!survival ? Rarity.EPIC : Rarity.COMMON), itemGroup);

			return new Blocks(new Blocks.WoodLog(log, stripped_log, twice_stripped_log), new Blocks.WoodLog(wood, stripped_wood, twice_stripped_wood), planks, stairs, slab, fence, fenceGate, door, trapdoor, pressurePlate, button, leaves);
		}

		public SupportedWoodItemSet build(boolean raft, EntityType<? extends AbstractBoatEntity> boatEntityType, EntityType<? extends AbstractBoatEntity> chestBoatEntityType) {
			Blocks blocks = buildBlocks(true);
			Blocks creativeBlocks = buildBlocks(false);
			Item bark = ItemRegistry.register(this.blockSet.id.withSuffixedPath("_bark"), Item::new, new Item.Settings());
			Item sapling = ItemRegistry.register(this.blockSet.id.withSuffixedPath("_sapling"), (settings) -> new BlockItem(this.blockSet.sapling, settings), new BlockItem.Settings().useBlockPrefixedTranslationKey());
			Item boat = boatEntityType != null ? ItemRegistry.register(this.blockSet.id.withSuffixedPath("_" + (raft ? "raft" : "boat")), (settings) -> new BoatItem(boatEntityType, settings), new Item.Settings().maxCount(1)) : null;
			Item chestBoat = chestBoatEntityType != null ? ItemRegistry.register(this.blockSet.id.withSuffixedPath("_chest_" + (raft ? "raft" : "boat")), (settings) -> new BoatItem(chestBoatEntityType, settings), new Item.Settings().maxCount(1)) : null;
			return new SupportedWoodItemSet(blocks, creativeBlocks, bark, sapling, boat, chestBoat);
		}
	}
}
