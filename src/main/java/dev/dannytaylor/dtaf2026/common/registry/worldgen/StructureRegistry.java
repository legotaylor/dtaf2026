/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.worldgen;

import com.mojang.serialization.MapCodec;
import dev.dannytaylor.dtaf2026.common.data.Data;
import dev.dannytaylor.dtaf2026.common.registry.worldgen.structure.MineshaftStructure;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

public class StructureRegistry {
	public static StructureType<MineshaftStructure> mineshaft = register("mineshaft", MineshaftStructure.CODEC);

	public static void bootstrap() {
	}

	public static <S extends Structure> StructureType<S> register(String path, MapCodec<S> codec) {
		return register(Data.idOf(path), codec);
	}

	public static <S extends Structure> StructureType<S> register(Identifier identifier, MapCodec<S> codec) {
		return Registry.register(Registries.STRUCTURE_TYPE, identifier, () -> codec);
	}
}
