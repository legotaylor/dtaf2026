/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.block;

import dev.dannytaylor.dtaf2026.common.data.Data;
import dev.dannytaylor.dtaf2026.common.registry.BlockRegistry;
import net.minecraft.block.*;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Direction;

import java.util.function.Function;

public class SupportedWoodBlockSet {
	public final Identifier id;
	public final SupportedLogBlock log;
	public final SupportedLogBlock wood;
	public final SupportedBlock planks;
	public final SupportedSlabBlock slab;
	public final SupportedLeavesBlock leaves;
	public final SaplingBlock sapling;

	private SupportedWoodBlockSet(Identifier id, SupportedLogBlock log, SupportedLogBlock wood, SupportedBlock planks, SupportedSlabBlock slab, SupportedLeavesBlock leaves, SaplingBlock sapling) {
		this.id = id;
		this.log = log;
		this.wood = wood;
		this.planks = planks;
		this.slab = slab;
		this.leaves = leaves;
		this.sapling = sapling;
	}

	public static Builder builder(String id) {
		return new Builder(Data.idOf(id));
	}

	public static Builder builder(Identifier id) {
		return new Builder(id);
	}

	public static AbstractBlock.Settings createLogSettings(Function<BlockState, MapColor> mapColor, BlockSoundGroup sounds, float strength, boolean burnable) {
		AbstractBlock.Settings settings = AbstractBlock.Settings.create().mapColor(mapColor).sounds(sounds).strength(strength).burnable();
		if (burnable) settings.burnable();
		return settings;
	}

	public static AbstractBlock.Settings createLogSettings(Function<BlockState, MapColor> mapColor, BlockSoundGroup sounds, boolean burnable) {
		return createLogSettings(mapColor, sounds, 3.0F, burnable);
	}

	public static MapColor getLogColor(BlockState state, MapColor topMapColor, MapColor sideMapColor) {
		return state.get(PillarBlock.AXIS) == Direction.Axis.Y ? topMapColor : sideMapColor;
	}

	public static AbstractBlock.Settings createPlanksSettings(Function<BlockState, MapColor> mapColor, NoteBlockInstrument instrument, float hardness, float resistance, BlockSoundGroup blockSoundGroup, boolean burnable) {
		AbstractBlock.Settings plankSettings = AbstractBlock.Settings.create().mapColor(mapColor).instrument(instrument).strength(hardness, resistance).sounds(BlockSoundGroup.WOOD);
		if (burnable) plankSettings.burnable();
		return plankSettings;
	}

	public static AbstractBlock.Settings getLogSettings(LogMapColor mapColor, BlockSoundGroup soundGroup, float strength, boolean burnable) {
		return createLogSettings((state) -> {
			int variant = state.get(SupportedLogBlock.variant);
			if (variant == 0) return getLogColor(state, mapColor.zeroMapColor.getLeft(), mapColor.zeroMapColor.getRight());
			else if (variant == 1) return getLogColor(state, mapColor.oneMapColor.getLeft(), mapColor.oneMapColor.getRight());
			else if (variant == 2) return getLogColor(state, mapColor.twoMapColor.getLeft(), mapColor.twoMapColor.getRight());
			else return MapColor.CLEAR;
		}, soundGroup, strength, burnable);
	}

	public static AbstractBlock.Settings getLogSettings(LogMapColor mapColor, BlockSoundGroup soundGroup) {
		return createLogSettings((state) -> {
			int variant = state.get(SupportedLogBlock.variant);
			if (variant == 0) return getLogColor(state, mapColor.zeroMapColor.getLeft(), mapColor.zeroMapColor.getRight());
			else if (variant == 1) return getLogColor(state, mapColor.oneMapColor.getLeft(), mapColor.oneMapColor.getRight());
			else if (variant == 2) return getLogColor(state, mapColor.twoMapColor.getLeft(), mapColor.twoMapColor.getRight());
			else return MapColor.CLEAR;
		}, soundGroup, 3.0F, true);
	}

	public static AbstractBlock.Settings getLogSettings(LogMapColor mapColor) {
		return getLogSettings(mapColor, BlockSoundGroup.WOOD);
	}

	public static AbstractBlock.Settings getLogSettings() {
		return getLogSettings(new LogMapColor(new Pair<>(MapColor.ORANGE, MapColor.IRON_GRAY), new Pair<>(MapColor.ORANGE, MapColor.ORANGE), new Pair<>(MapColor.ORANGE, MapColor.ORANGE)), BlockSoundGroup.WOOD);
	}

	public static AbstractBlock.Settings getPlanksSettings(Function<BlockState, MapColor> mapColor) {
		return createPlanksSettings(mapColor, NoteBlockInstrument.BASS, 3.0F, 4.0F, BlockSoundGroup.WOOD, true);
	}

	public static AbstractBlock.Settings getPlanksSettings() {
		return getPlanksSettings((state) -> MapColor.ORANGE);
	}

	public static AbstractBlock.Settings getLeavesSettings(MapColor mapColor, float strength, BlockSoundGroup soundGroup, AbstractBlock.TypedContextPredicate<EntityType<?>> allowsSpawning, AbstractBlock.ContextPredicate suffocates, PistonBehavior pistonBehavior, AbstractBlock.ContextPredicate solidBlock) {
		return AbstractBlock.Settings.create().mapColor(mapColor).strength(strength).sounds(soundGroup).nonOpaque().allowsSpawning(allowsSpawning).suffocates(suffocates).blockVision(Blocks::never).burnable().pistonBehavior(pistonBehavior).solidBlock(solidBlock);
	}

	public static AbstractBlock.Settings getLeavesSettings(MapColor mapColor, BlockSoundGroup soundGroup) {
		return getLeavesSettings(mapColor, 0.2F, soundGroup, Blocks::canSpawnOnLeaves, Blocks::never, PistonBehavior.DESTROY, Blocks::never);
	}

	public static AbstractBlock.Settings getLeavesSettings() {
		return getLeavesSettings(MapColor.DARK_RED, BlockSoundGroup.GRASS);
	}

	public static SaplingGenerator getSaplingGenerator() {
		return SaplingGenerator.OAK;
	}

	public static AbstractBlock.Settings getSaplingSettings(MapColor mapColor, BlockSoundGroup soundGroup, PistonBehavior pistonBehavior) {
		return AbstractBlock.Settings.create().mapColor(mapColor).noCollision().ticksRandomly().breakInstantly().sounds(soundGroup).pistonBehavior(pistonBehavior);
	}

	public static AbstractBlock.Settings getSaplingSettings(MapColor mapColor) {
		return getSaplingSettings(mapColor, BlockSoundGroup.GRASS, PistonBehavior.DESTROY);
	}

	public static AbstractBlock.Settings getSaplingSettings() {
		return getSaplingSettings(MapColor.DARK_RED);
	}

	public static class Builder {
		private final Identifier id;
		private AbstractBlock.Settings logSettings;
		private AbstractBlock.Settings woodSettings;
		private AbstractBlock.Settings planksSettings;
		private AbstractBlock.Settings slabSettings;
		private AbstractBlock.Settings leavesSettings;
		private SaplingGenerator saplingGenerator;
		private AbstractBlock.Settings saplingSettings;

		public Builder(Identifier id) {
			this(id, getLogSettings(), getLogSettings(), getPlanksSettings(), getLeavesSettings(), getSaplingGenerator(), getSaplingSettings());
		}

		public Builder(Identifier id, LogMapColor log, MapColor planks, MapColor sapling) {
			this(id, getLogSettings(log), getLogSettings(log), getPlanksSettings((state) -> planks), getPlanksSettings(), getLeavesSettings(), getSaplingGenerator(), getSaplingSettings(sapling));
		}

		public Builder(Identifier id, AbstractBlock.Settings logSettings, AbstractBlock.Settings woodSettings, AbstractBlock.Settings planksSettings, AbstractBlock.Settings leavesSettings, SaplingGenerator saplingGenerator, AbstractBlock.Settings saplingSettings) {
			this(id, logSettings, woodSettings, planksSettings, planksSettings, leavesSettings, saplingGenerator, saplingSettings);
		}

		public Builder(Identifier id, AbstractBlock.Settings logSettings, AbstractBlock.Settings woodSettings, AbstractBlock.Settings planksSettings, AbstractBlock.Settings slabSettings, AbstractBlock.Settings leavesSettings, SaplingGenerator saplingGenerator, AbstractBlock.Settings saplingSettings) {
			this.id = id;
			this.logSettings = logSettings;
			this.woodSettings = woodSettings;
			this.planksSettings = planksSettings;
			this.slabSettings = slabSettings;
			this.leavesSettings = leavesSettings;
			this.saplingGenerator = saplingGenerator;
			this.saplingSettings = saplingSettings;
		}

		public Builder logSettings(AbstractBlock.Settings logSettings) {
			this.logSettings = logSettings;
			return this;
		}

		public Builder woodSettings(AbstractBlock.Settings woodSettings) {
			this.woodSettings = woodSettings;
			return this;
		}

		public Builder planksSettings(AbstractBlock.Settings planksSettings) {
			this.planksSettings = planksSettings;
			return this;
		}

		public Builder slabSettings(AbstractBlock.Settings slabSettings) {
			this.slabSettings = slabSettings;
			return this;
		}

		public Builder leavesSettings(AbstractBlock.Settings leavesSettings) {
			this.leavesSettings = leavesSettings;
			return this;
		}

		public Builder saplingGenerator(SaplingGenerator saplingGenerator) {
			this.saplingGenerator = saplingGenerator;
			return this;
		}

		public Builder saplingSettings(AbstractBlock.Settings saplingSettings) {
			this.saplingSettings = saplingSettings;
			return this;
		}

		public SupportedWoodBlockSet build() {
			SupportedLogBlock log = (SupportedLogBlock) BlockRegistry.register(this.id.withSuffixedPath("_log"), SupportedLogBlock::new, this.logSettings);
			SupportedLogBlock wood = (SupportedLogBlock) BlockRegistry.register(this.id.withSuffixedPath("_wood"), SupportedLogBlock::new, this.woodSettings);
			SupportedBlock planks = (SupportedBlock) BlockRegistry.register(this.id.withSuffixedPath("_planks"), SupportedBlock::new, this.planksSettings);
			SupportedSlabBlock slab = (SupportedSlabBlock) BlockRegistry.register(this.id.withSuffixedPath("_slab"), SupportedSlabBlock::new, this.slabSettings);
			SupportedLeavesBlock leaves = (SupportedLeavesBlock) BlockRegistry.register(this.id.withSuffixedPath("_leaves"), (settings) -> new SupportedLeavesBlock(0.01F, ParticleTypes.PALE_OAK_LEAVES, settings), this.leavesSettings);
			SaplingBlock sapling = (SaplingBlock) BlockRegistry.register(this.id.withSuffixedPath("_sapling"), (settings) -> new SaplingBlock(this.saplingGenerator, settings), this.saplingSettings);
			return new SupportedWoodBlockSet(this.id, log, wood, planks, slab, leaves, sapling);
		}
	}
	public record LogMapColor(Pair<MapColor, MapColor> zeroMapColor, Pair<MapColor, MapColor> oneMapColor, Pair<MapColor, MapColor> twoMapColor) {}
}
