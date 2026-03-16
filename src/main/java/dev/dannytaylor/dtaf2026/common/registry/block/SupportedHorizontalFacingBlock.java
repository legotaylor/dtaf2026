/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.Direction;

public class SupportedHorizontalFacingBlock extends SupportedBlock {
	public static final MapCodec<SupportedHorizontalFacingBlock> codec = RecordCodecBuilder.mapCodec(
		instance -> instance.group(
				TagKey.codec(RegistryKeys.BLOCK).fieldOf("isOf").forGetter(block -> block.isOf),
				createSettingsCodec()
			)
			.apply(instance, SupportedHorizontalFacingBlock::new)
	);

	public static final EnumProperty<Direction> facing = Properties.HORIZONTAL_FACING;

	public SupportedHorizontalFacingBlock(TagKey<Block> isOf, Settings settings) {
		super(isOf, settings);
	}

	public MapCodec<? extends SupportedHorizontalFacingBlock> getCodec() {
		return codec;
	}

	protected BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(facing, rotation.rotate(state.get(facing)));
	}

	protected BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(facing)));
	}
}
