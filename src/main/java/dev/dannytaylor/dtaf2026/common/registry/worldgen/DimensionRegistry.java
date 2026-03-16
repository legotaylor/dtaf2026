/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.worldgen;

import dev.dannytaylor.dtaf2026.common.data.Data;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;

import java.util.ArrayList;
import java.util.List;

public class DimensionRegistry {
	public static final List<Identifier> isAlwaysNight;
	public static final Dimension somniumReale;
	public static final Dimension theTerrorlands;

	static {
		isAlwaysNight = new ArrayList<>();
		somniumReale = new Dimension(Data.getSomniumRealeId());
		theTerrorlands = new Dimension(Data.getSomniaMetusId());
	}

	public static void bootstrap() {
	}

	public static BlockPos toHighestBlockPos(ServerWorld serverWorld, BlockPos pos) {
		for (int i = serverWorld.getTopYInclusive(); i > serverWorld.getBottomY(); i--) { // spawn at the top most non-air block.
			BlockPos checkPos = pos.withY(i);
			if (!serverWorld.getBlockState(checkPos).isAir()) {
				pos = checkPos.up();
				break;
			}
		}
		return pos;
	}

	public static class Dimension {
		private final Identifier id;
		private final RegistryKey<net.minecraft.world.World> world;
		private final RegistryKey<DimensionOptions> options;
		private final RegistryKey<DimensionType> type;

		public Dimension(Identifier id) {
			this(id, true);
		}

		public Dimension(Identifier id, boolean alwaysNight) {
			this.id = id;
			this.world = RegistryKey.of(RegistryKeys.WORLD, id);
			this.options = RegistryKey.of(RegistryKeys.DIMENSION, id);
			this.type = RegistryKey.of(RegistryKeys.DIMENSION_TYPE, id);
			if (alwaysNight) DimensionRegistry.isAlwaysNight.add(id);
		}

		public Identifier id() {
			return this.id;
		}

		public RegistryKey<net.minecraft.world.World> world() {
			return this.world;
		}

		public RegistryKey<DimensionOptions> options() {
			return this.options;
		}

		public RegistryKey<DimensionType> type() {
			return this.type;
		}
	}
}
