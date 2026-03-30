package dev.dannytaylor.dtaf2026.common.registry.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ExtractorBlock extends AbstractFurnaceBlock {
	public static final MapCodec<ExtractorBlock> CODEC = createCodec(ExtractorBlock::new);

	public MapCodec<ExtractorBlock> getCodec() {
		return CODEC;
	}

	public ExtractorBlock(AbstractBlock.Settings settings) {
		super(settings);
	}

	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new ExtractorBlockEntity(pos, state);
	}

	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return validateTicker(world, type, BlockEntityRegistry.extractor);
	}

	protected void openScreen(World world, BlockPos pos, PlayerEntity player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof ExtractorBlockEntity) {
			player.openHandledScreen((NamedScreenHandlerFactory) blockEntity);
			player.incrementStat(Stats.INTERACT_WITH_BLAST_FURNACE); //todo
		}

	}

	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		if (state.get(LIT)) {
			double d = (double) pos.getX() + (double) 0.5F;
			double e = pos.getY();
			double f = (double) pos.getZ() + (double) 0.5F;
			if (random.nextDouble() < 0.1) {
				world.playSoundClient(d, e, f, SoundEvents.BLOCK_BLASTFURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
			}

			Direction direction = state.get(FACING);
			Direction.Axis axis = direction.getAxis();
			double g = 0.52;
			double h = random.nextDouble() * 0.6 - 0.3;
			double i = axis == Direction.Axis.X ? (double) direction.getOffsetX() * 0.52 : h;
			double j = random.nextDouble() * (double) 9.0F / (double) 16.0F;
			double k = axis == Direction.Axis.Z ? (double) direction.getOffsetZ() * 0.52 : h;
			world.addParticleClient(ParticleTypes.SMOKE, d + i, e + j, f + k, 0.0F, 0.0F, 0.0F);
		}
	}
}
