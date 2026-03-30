package dev.dannytaylor.dtaf2026.common.registry.block;

import dev.dannytaylor.dtaf2026.common.data.Data;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;

public class BlockEntityRegistry {
	public static final BlockEntityType<ExtractorBlockEntity> extractor;

	public static void bootstrap() {
	}

	public static <T extends BlockEntity> BlockEntityType<T> create(String path, BlockEntityType.BlockEntityFactory<? extends T> factory, Block... blocks) {
		return create(Data.idOf(path), factory, blocks);
	}

	public static <T extends BlockEntity> BlockEntityType<T> create(Identifier identifier, BlockEntityType.BlockEntityFactory<? extends T> factory, Block... blocks) {
		return BlockEntityType.create(identifier.toString(), factory, blocks);
	}

	static {
		extractor = create("extractor", ExtractorBlockEntity::new, BlockRegistry.extractor);
	}
}
