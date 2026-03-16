/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.block;

import dev.dannytaylor.dtaf2026.common.data.Data;
import dev.dannytaylor.dtaf2026.common.registry.particle.ParticleRegistry;
import dev.dannytaylor.dtaf2026.common.registry.worldgen.SaplingGeneratorRegistry;
import net.minecraft.block.*;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Direction;

import java.util.function.Function;

public class SupportedWoodBlockSet {
	public final Identifier id;
	public final BlockSetType blockSetType;
	public final WoodType woodType;
	public final SupportedLogBlock log;
	public final SupportedLogBlock wood;
	public final SupportedBlock planks;
	public final SupportedSlabBlock slab;
	public final SupportedStairsBlock stairs;
	public final SupportedLeavesBlock leaves;
	public final SaplingBlock sapling;
	public final DoorBlock door;
	public final TrapdoorBlock trapdoor;
	public final SupportedFenceBlock fence;
	public final SupportedFenceGateBlock fenceGate;
	public final PressurePlateBlock pressurePlate;
	public final ButtonBlock button;
	public final boolean flammable;

	private SupportedWoodBlockSet(Identifier id,
								  BlockSetType blockSetType,
								  WoodType woodType,
								  SupportedLogBlock log,
								  SupportedLogBlock wood,
								  SupportedBlock planks,
								  SupportedSlabBlock slab,
								  SupportedStairsBlock stairs,
								  SupportedLeavesBlock leaves,
								  SaplingBlock sapling,
								  DoorBlock door,
								  TrapdoorBlock trapdoor,
								  SupportedFenceBlock fence,
								  SupportedFenceGateBlock fenceGate,
								  PressurePlateBlock pressurePlate,
								  ButtonBlock button,
								  boolean flammable) {
		this.id = id;
		this.blockSetType = blockSetType;
		this.woodType = woodType;
		this.log = log;
		this.wood = wood;
		this.planks = planks;
		this.slab = slab;
		this.stairs = stairs;
		this.leaves = leaves;
		this.sapling = sapling;
		this.door = door;
		this.trapdoor = trapdoor;
		this.fence = fence;
		this.fenceGate = fenceGate;
		this.pressurePlate = pressurePlate;
		this.button = button;
		this.flammable = flammable;
	}

	public static Builder builder(String id) {
		return new Builder(Data.idOf(id));
	}

	public static Builder builder(Identifier id) {
		return new Builder(id);
	}

	public static Builder builder(String id, boolean flammable) {
		return new Builder(Data.idOf(id), flammable);
	}

	public static Builder builder(Identifier id, boolean flammable) {
		return new Builder(id, flammable);
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
		return SaplingGeneratorRegistry.maple;
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

	public static boolean getFlammable() {
		return true;
	}

	public static ParticleEffect getLeafParticles() {
		return ParticleRegistry.mapleLeaves;
	}

	public static class Builder {
		private final Identifier id;
		private AbstractBlock.Settings logSettings;
		private AbstractBlock.Settings woodSettings;
		private AbstractBlock.Settings planksSettings;
		private AbstractBlock.Settings slabSettings;
		private AbstractBlock.Settings stairsSettings;
		private AbstractBlock.Settings leavesSettings;
		private SaplingGenerator saplingGenerator;
		private AbstractBlock.Settings saplingSettings;
		private boolean flammable;
		private ParticleEffect leafParticles;
		private AbstractBlock.Settings fenceSettings;
		private AbstractBlock.Settings fenceGateSettings;

		public Builder(Identifier id, boolean flammable) {
			this(id, getLogSettings(), getLogSettings(), getPlanksSettings(), getLeavesSettings(), getSaplingGenerator(), getSaplingSettings(), flammable);
		}

		public Builder(Identifier id) {
			this(id, getFlammable());
		}

		public Builder(Identifier id, LogMapColor log, MapColor planks, MapColor sapling, boolean flammable) {
			this(id, getLogSettings(log), getLogSettings(log), getPlanksSettings((state) -> planks), getPlanksSettings(), getPlanksSettings(), getLeavesSettings(), getSaplingGenerator(), getSaplingSettings(sapling), flammable, getLeafParticles(), getPlanksSettings(), getPlanksSettings());
		}

		public Builder(Identifier id, LogMapColor log, MapColor planks, MapColor sapling) {
			this(id, log, planks, sapling, getFlammable());
		}

		public Builder(Identifier id, AbstractBlock.Settings logSettings, AbstractBlock.Settings woodSettings, AbstractBlock.Settings planksSettings, AbstractBlock.Settings leavesSettings, SaplingGenerator saplingGenerator, AbstractBlock.Settings saplingSettings, boolean flammable) {
			this(id, logSettings, woodSettings, planksSettings, planksSettings, planksSettings, leavesSettings, saplingGenerator, saplingSettings, flammable, getLeafParticles(), planksSettings, planksSettings);
		}

		public Builder(Identifier id, AbstractBlock.Settings logSettings, AbstractBlock.Settings woodSettings, AbstractBlock.Settings planksSettings, AbstractBlock.Settings leavesSettings, SaplingGenerator saplingGenerator, AbstractBlock.Settings saplingSettings) {
			this(id, logSettings, woodSettings, planksSettings, leavesSettings, saplingGenerator, saplingSettings, getFlammable());
		}

		public Builder(Identifier id, AbstractBlock.Settings logSettings, AbstractBlock.Settings woodSettings, AbstractBlock.Settings planksSettings, AbstractBlock.Settings slabSettings, AbstractBlock.Settings stairsSettings, AbstractBlock.Settings leavesSettings, SaplingGenerator saplingGenerator, AbstractBlock.Settings saplingSettings, boolean flammable, ParticleEffect leafParticles, AbstractBlock.Settings fenceSettings, AbstractBlock.Settings fenceGateSettings) {
			this.id = id;
			this.logSettings = logSettings;
			this.woodSettings = woodSettings;
			this.planksSettings = planksSettings;
			this.slabSettings = slabSettings;
			this.stairsSettings = stairsSettings;
			this.leavesSettings = leavesSettings;
			this.saplingGenerator = saplingGenerator;
			this.saplingSettings = saplingSettings;
			this.flammable = flammable;
			this.leafParticles = leafParticles;
			this.fenceSettings = fenceSettings;
			this.fenceGateSettings = fenceGateSettings;
		}

		public Builder(Identifier id, AbstractBlock.Settings logSettings, AbstractBlock.Settings woodSettings, AbstractBlock.Settings planksSettings, AbstractBlock.Settings slabSettings, AbstractBlock.Settings stairsSettings, AbstractBlock.Settings leavesSettings, SaplingGenerator saplingGenerator, AbstractBlock.Settings saplingSettings, AbstractBlock.Settings fenceSettings, AbstractBlock.Settings fenceGateSettings) {
			this(id, logSettings, woodSettings, planksSettings, slabSettings, stairsSettings, leavesSettings, saplingGenerator, saplingSettings, getFlammable(), getLeafParticles(), fenceSettings, fenceGateSettings);
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

		public Builder stairsSettings(AbstractBlock.Settings stairsSettings) {
			this.stairsSettings = stairsSettings;
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

		public Builder flammable(boolean flammable) {
			this.flammable = flammable;
			return this;
		}

		public Builder leaveParticles(SimpleParticleType leafParticles) {
			this.leafParticles = leafParticles;
			return this;
		}

		public Builder fenceSettings(AbstractBlock.Settings fenceSettings) {
			this.fenceSettings = fenceSettings;
			return this;
		}

		public Builder fenceGateSettings(AbstractBlock.Settings fenceGateSettings) {
			this.fenceGateSettings = fenceGateSettings;
			return this;
		}

		public SupportedWoodBlockSet build(BlockSetTypeBuilder typeBuilder) {
			return build(0.01F, typeBuilder);
		}

		public SupportedWoodBlockSet build(float leafParticleChance) {
			return build(leafParticleChance, new BlockSetTypeBuilder());
		}

		public SupportedWoodBlockSet build(float leafParticleChance, BlockSetTypeBuilder blockSetTypeBuilder) {
			BlockSetType blockSetType = BlockSetType.register(new BlockSetType(this.id.toString(), blockSetTypeBuilder.canOpenByHand, blockSetTypeBuilder.canOpenByWindCharge, blockSetTypeBuilder.canButtonBeActivatedByArrows, blockSetTypeBuilder.pressurePlateSensitivity, blockSetTypeBuilder.soundType, blockSetTypeBuilder.doorClose, blockSetTypeBuilder.doorOpen, blockSetTypeBuilder.trapdoorClose, blockSetTypeBuilder.trapdoorOpen, blockSetTypeBuilder.pressurePlateClickOff, blockSetTypeBuilder.pressurePlateClickOn, blockSetTypeBuilder.buttonClickOff, blockSetTypeBuilder.buttonClickOn));
			WoodType woodType = WoodTypeRegistry.register(this.id.toUnderscoreSeparatedString(), blockSetType);
			TagKey<Block> tagKey = TagKey.of(RegistryKeys.BLOCK, this.id.withPrefixedPath("supported/wood_set/"));
			SupportedLogBlock log = BlockRegistry.register(this.id.withSuffixedPath("_log"), (settings) -> new SupportedLogBlock(tagKey, settings), this.logSettings);
			SupportedLogBlock wood = BlockRegistry.register(this.id.withSuffixedPath("_wood"), (settings) -> new SupportedLogBlock(tagKey, settings), this.woodSettings);
			SupportedBlock planks = BlockRegistry.register(this.id.withSuffixedPath("_planks"), (settings) -> new SupportedBlock(tagKey, settings), this.planksSettings);
			SupportedSlabBlock slab = BlockRegistry.register(this.id.withSuffixedPath("_slab"), (settings) -> new SupportedSlabBlock(tagKey, settings), this.slabSettings);
			SupportedStairsBlock stairs = BlockRegistry.register(this.id.withSuffixedPath("_stairs"), (settings) -> new SupportedStairsBlock(planks.getDefaultState(), tagKey, settings), this.stairsSettings);
			SupportedLeavesBlock leaves = BlockRegistry.register(this.id.withSuffixedPath("_leaves"), (settings) -> new SupportedLeavesBlock(leafParticleChance, this.leafParticles, tagKey, settings), this.leavesSettings);
			SaplingBlock sapling = BlockRegistry.register(this.id.withSuffixedPath("_sapling"), (settings) -> new SaplingBlock(this.saplingGenerator, settings), this.saplingSettings);
			DoorBlock door = BlockRegistry.register(this.id.withSuffixedPath("_door"), (settings) -> new DoorBlock(blockSetType, settings), AbstractBlock.Settings.create().mapColor(planks.getDefaultMapColor()).instrument(NoteBlockInstrument.BASS).strength(3.0F).nonOpaque().burnable().pistonBehavior(PistonBehavior.DESTROY));
			TrapdoorBlock trapdoor = BlockRegistry.register(this.id.withSuffixedPath("_trapdoor"), (settings) -> new TrapdoorBlock(blockSetType, settings), AbstractBlock.Settings.create().mapColor(planks.getDefaultMapColor()).instrument(NoteBlockInstrument.BASS).strength(3.0F).nonOpaque().allowsSpawning(Blocks::never).burnable());
			SupportedFenceBlock fence = BlockRegistry.register(this.id.withSuffixedPath("_fence"), (settings) -> new SupportedFenceBlock(tagKey, settings), this.fenceSettings);
			SupportedFenceGateBlock fenceGate = BlockRegistry.register(this.id.withSuffixedPath("_fence_gate"), (settings) -> new SupportedFenceGateBlock(woodType, tagKey, settings), this.fenceGateSettings);
			PressurePlateBlock pressurePlate = BlockRegistry.register(this.id.withSuffixedPath("_pressure_plate"), (settings) -> new PressurePlateBlock(blockSetType, settings), AbstractBlock.Settings.create().mapColor(planks.getDefaultMapColor()).solid().instrument(NoteBlockInstrument.BASS).noCollision().strength(0.5F).burnable().pistonBehavior(PistonBehavior.DESTROY));
			ButtonBlock button = BlockRegistry.register(this.id.withSuffixedPath("_button"), (settings) -> new ButtonBlock(blockSetType, 30, settings), Blocks.createButtonSettings());
			return new SupportedWoodBlockSet(this.id, blockSetType, woodType, log, wood, planks, slab, stairs, leaves, sapling, door, trapdoor, fence, fenceGate, pressurePlate, button, this.flammable);
		}
	}
	public record LogMapColor(Pair<MapColor, MapColor> zeroMapColor, Pair<MapColor, MapColor> oneMapColor, Pair<MapColor, MapColor> twoMapColor) {}

	public record BlockSetTypeBuilder(boolean canOpenByHand, boolean canOpenByWindCharge,
									  boolean canButtonBeActivatedByArrows,
									  BlockSetType.ActivationRule pressurePlateSensitivity, BlockSoundGroup soundType,
									  SoundEvent doorClose, SoundEvent doorOpen, SoundEvent trapdoorClose,
									  SoundEvent trapdoorOpen, SoundEvent pressurePlateClickOff,
									  SoundEvent pressurePlateClickOn, SoundEvent buttonClickOff,
									  SoundEvent buttonClickOn) {
		BlockSetTypeBuilder() {
			this(true, true, true, BlockSetType.ActivationRule.EVERYTHING, BlockSoundGroup.WOOD, SoundEvents.BLOCK_WOODEN_DOOR_CLOSE, SoundEvents.BLOCK_WOODEN_DOOR_OPEN, SoundEvents.BLOCK_WOODEN_TRAPDOOR_CLOSE, SoundEvents.BLOCK_WOODEN_TRAPDOOR_OPEN, SoundEvents.BLOCK_WOODEN_PRESSURE_PLATE_CLICK_OFF, SoundEvents.BLOCK_WOODEN_PRESSURE_PLATE_CLICK_ON, SoundEvents.BLOCK_WOODEN_BUTTON_CLICK_OFF, SoundEvents.BLOCK_WOODEN_BUTTON_CLICK_ON);
		}
	}
}
