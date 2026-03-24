/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.worldgen.structure;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.dannytaylor.dtaf2026.common.registry.worldgen.StructureRegistry;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.structure.StructurePiecesCollector;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

import java.util.Optional;

public class MineshaftStructure extends Structure {
	public static final MapCodec<MineshaftStructure> CODEC = RecordCodecBuilder.mapCodec(instance ->
		instance.group(
			configCodecBuilder(instance),
			Data.CODEC.fieldOf("mineshaft_data").forGetter(mineshaft -> mineshaft.data)
		).apply(instance, MineshaftStructure::new)
	);

	private final Data data;

	public MineshaftStructure(Structure.Config config, Data data) {
		super(config);
		this.data = data;
	}

	public Optional<Structure.StructurePosition> getStructurePosition(Structure.Context context) {
		context.random().nextDouble();
		ChunkPos chunkPos = context.chunkPos();
		BlockPos blockPos = new BlockPos(chunkPos.getCenterX(), 50, chunkPos.getStartZ());
		StructurePiecesCollector structurePiecesCollector = new StructurePiecesCollector();
		int i = this.addPieces(structurePiecesCollector, context);
		return Optional.of(new Structure.StructurePosition(blockPos.add(0, i, 0), Either.right(structurePiecesCollector)));
	}

	private int addPieces(StructurePiecesCollector collector, Structure.Context context) {
		ChunkPos chunkPos = context.chunkPos();
		ChunkRandom chunkRandom = context.random();
		ChunkGenerator chunkGenerator = context.chunkGenerator();
		MineshaftGenerator.MineshaftRoom mineshaftRoom = new MineshaftGenerator.MineshaftRoom(0, chunkRandom, chunkPos.getOffsetX(2), chunkPos.getOffsetZ(2), this.data);
		collector.addPiece(mineshaftRoom);
		mineshaftRoom.fillOpenings(mineshaftRoom, collector, chunkRandom);
		return collector.shiftInto(chunkGenerator.getSeaLevel(), chunkGenerator.getMinimumY(), chunkRandom, 10);
	}

	public StructureType<?> getType() {
		return StructureRegistry.mineshaft;
	}

	public record Data(Identifier log, Identifier planks, Identifier fence) {
		public static MapCodec<Data> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
			Identifier.CODEC.fieldOf("log").forGetter((data) -> data.log),
			Identifier.CODEC.fieldOf("planks").forGetter((data) -> data.planks),
			Identifier.CODEC.fieldOf("fence").forGetter((data) -> data.fence)
		).apply(instance, Data::new));

		public Block getLog() {
			return Registries.BLOCK.get(this.log());
		}

		public Block getPlanks() {
			return Registries.BLOCK.get(this.planks());
		}

		public Block getFence() {
			return Registries.BLOCK.get(this.fence());
		}
	}
}
