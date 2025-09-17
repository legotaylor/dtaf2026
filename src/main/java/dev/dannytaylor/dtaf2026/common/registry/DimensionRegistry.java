/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry;

import dev.dannytaylor.dtaf2026.common.data.Data;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;

public class DimensionRegistry {
	public static final Dimension somniumReale;

	public static void bootstrap() {
	}

	static {
		somniumReale = new Dimension(Data.getSomniumRealeId());
	}

	public record Dimension(Identifier id) {
		public RegistryKey<net.minecraft.world.World> world() {
			return RegistryKey.of(RegistryKeys.WORLD, id);
		}

		public RegistryKey<DimensionOptions> options() {
			return RegistryKey.of(RegistryKeys.DIMENSION, id);
		}

		public RegistryKey<DimensionType> type() {
			return RegistryKey.of(RegistryKeys.DIMENSION_TYPE, id);
		}
	}
}
