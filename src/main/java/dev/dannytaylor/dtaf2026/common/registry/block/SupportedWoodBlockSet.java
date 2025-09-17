package dev.dannytaylor.dtaf2026.common.registry.block;

import dev.dannytaylor.dtaf2026.common.data.Data;
import dev.dannytaylor.dtaf2026.common.registry.BlockRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class SupportedWoodBlockSet {
	public final Identifier id;
	public final SupportedLogBlock log;
	public final SupportedLogBlock wood;
	public final SupportedBlock planks;
	public final SupportedSlabBlock slab;

	private SupportedWoodBlockSet(Identifier id, SupportedLogBlock log, SupportedLogBlock wood, SupportedBlock planks, SupportedSlabBlock slab) {
		this.id = id;
		this.log = log;
		this.wood = wood;
		this.planks = planks;
		this.slab = slab;
	}

	public static Builder builder(String id) {
		return new Builder(Data.idOf(id));
	}

	public static Builder builder(Identifier id) {
		return new Builder(id);
	}

	public static AbstractBlock.Settings createLogSettings(MapColor topMapColor, MapColor sideMapColor, BlockSoundGroup sounds, float strength) {
		return Blocks.createLogSettings(topMapColor, sideMapColor, sounds).strength(strength);
	}

	public static AbstractBlock.Settings createLogSettings(MapColor topMapColor, MapColor sideMapColor, BlockSoundGroup sounds) {
		return createLogSettings(topMapColor, sideMapColor, sounds, 3.0F);
	}

	public static AbstractBlock.Settings createPlanksSettings(MapColor mapColor, NoteBlockInstrument instrument, float hardness, float resistance, BlockSoundGroup blockSoundGroup, boolean burnable) {
		AbstractBlock.Settings plankSettings = AbstractBlock.Settings.create().mapColor(mapColor).instrument(instrument).strength(2.0F, 3.0F).sounds(BlockSoundGroup.WOOD);
		if (burnable) plankSettings.burnable();
		return plankSettings;
	}

	public static AbstractBlock.Settings getDefaultLogSettings(MapColor topMapColor, MapColor sideMapColor) {
		return createLogSettings(topMapColor, sideMapColor, BlockSoundGroup.WOOD);
	}

	public static AbstractBlock.Settings getDefaultLogSettings() {
		return getDefaultLogSettings(MapColor.OAK_TAN, MapColor.SPRUCE_BROWN);
	}

	public static AbstractBlock.Settings getDefaultPlanksSettings(MapColor mapColor) {
		return createPlanksSettings(mapColor, NoteBlockInstrument.BASS, 3.0F, 4.0F, BlockSoundGroup.WOOD, true);
	}

	public static AbstractBlock.Settings getDefaultPlanksSettings() {
		return getDefaultPlanksSettings(MapColor.OAK_TAN);
	}

	public static class Builder {
		private final Identifier id;
		private AbstractBlock.Settings logSettings;
		private AbstractBlock.Settings woodSettings;
		private AbstractBlock.Settings planksSettings;
		private AbstractBlock.Settings slabSettings;

		public Builder(Identifier id) {
			this(id, getDefaultLogSettings(), getDefaultLogSettings(), getDefaultPlanksSettings());
		}

		public Builder(Identifier id, MapColor logSide, MapColor logTop, MapColor planks) {
			this(id, getDefaultLogSettings(logTop, logSide), getDefaultLogSettings(logTop, logSide), getDefaultPlanksSettings(planks));
		}

		public Builder(Identifier id, AbstractBlock.Settings logSettings, AbstractBlock.Settings woodSettings, AbstractBlock.Settings planksSettings) {
			this(id, logSettings, woodSettings, planksSettings, planksSettings);
		}

		public Builder(Identifier id, AbstractBlock.Settings logSettings, AbstractBlock.Settings woodSettings, AbstractBlock.Settings planksSettings, AbstractBlock.Settings slabSettings) {
			this.id = id;
			this.logSettings = logSettings;
			this.woodSettings = woodSettings;
			this.planksSettings = planksSettings;
			this.slabSettings = slabSettings;
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

		public SupportedWoodBlockSet build() {
			SupportedLogBlock log = (SupportedLogBlock) BlockRegistry.register(this.id.withSuffixedPath("_log"), SupportedLogBlock::new, this.logSettings);
			SupportedLogBlock wood = (SupportedLogBlock) BlockRegistry.register(this.id.withSuffixedPath("_wood"), SupportedLogBlock::new, this.woodSettings);
			SupportedBlock planks = (SupportedBlock) BlockRegistry.register(this.id.withSuffixedPath("_planks"), SupportedBlock::new, this.planksSettings);
			SupportedSlabBlock slab = (SupportedSlabBlock) BlockRegistry.register(this.id.withSuffixedPath("_slab"), SupportedSlabBlock::new, this.slabSettings);
			return new SupportedWoodBlockSet(this.id, log, wood, planks, slab);
		}
	}
}
