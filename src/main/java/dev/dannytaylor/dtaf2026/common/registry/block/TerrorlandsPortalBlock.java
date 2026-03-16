/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.block;

import com.mojang.serialization.MapCodec;
import dev.dannytaylor.dtaf2026.client.data.ClientData;
import dev.dannytaylor.dtaf2026.common.registry.SoundEventRegistry;
import dev.dannytaylor.dtaf2026.common.registry.worldgen.DimensionRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Portal;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCollisionHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class TerrorlandsPortalBlock extends Block implements Portal {
	public static final MapCodec<TerrorlandsPortalBlock> codec = createCodec(TerrorlandsPortalBlock::new);
	private static final VoxelShape shape;

	static {
		shape = Block.createColumnShape(16.0F, 0.0F, 8.0F);
	}

	public TerrorlandsPortalBlock(Settings settings) {
		super(settings);
	}

	public MapCodec<TerrorlandsPortalBlock> getCodec() {
		return codec;
	}

	public TeleportTarget createTeleportTarget(ServerWorld world, Entity entity, BlockPos pos) {
		ServerWorld theTerrorlands = world.getServer().getWorld(DimensionRegistry.theTerrorlands.world());
		if (theTerrorlands != null) {
			return new TeleportTarget(theTerrorlands, DimensionRegistry.toHighestBlockPos(theTerrorlands, theTerrorlands.getSpawnPos()).toBottomCenterPos(), Vec3d.ZERO, 0.0F, 0.0F, PositionFlag.combine(PositionFlag.DELTA, PositionFlag.ROT), (e) -> {
			});
		}
		return null;
	}

	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		if (random.nextInt(16) == 0)
			world.playSoundClient((double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5, SoundEventRegistry.portalAmbient, SoundCategory.BLOCKS, 0.5F, random.nextFloat() * 0.4F + 0.8F, false);
		emitParticles(world, pos, random, ParticleTypes.SMOKE, 16, false);
	}

	public void emitParticles(World world, BlockPos pos, Random random, ParticleEffect effect, int amount, boolean hasYVelocity) {
		for (int index = 0; index < amount; ++index) {
			double x = (double) pos.getX() + random.nextDouble();
			double y = (double) pos.getY() + random.nextDouble();
			double z = (double) pos.getZ() + random.nextDouble();
			world.addParticleClient(effect, x, y, z, 0.0, hasYVelocity ? (world.random.nextDouble() - 0.5) * 0.5 : 0.0, 0.0);
		}
	}

	@Override
	protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, EntityCollisionHandler handler) {
		if (entity.canUsePortals(false)) {
			if (world.isClient && entity.equals(ClientData.getMinecraft().getCameraEntity()))
				world.playSoundClient(SoundEventRegistry.portalTeleport, SoundCategory.BLOCKS, 0.5F, world.random.nextFloat() * 0.4F + 0.8F);
			else
				world.playSoundClient((double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5, SoundEventRegistry.portalTeleport, SoundCategory.BLOCKS, 0.5F, world.random.nextFloat() * 0.4F + 0.8F, false);
			emitParticles(world, pos, world.random, ParticleTypes.SMOKE, 128, true);
			entity.tryUsePortal(this, pos);
		}
	}

	protected ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state, boolean includeData) {
		return ItemStack.EMPTY;
	}

	@Override
	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return shape;
	}

	@Override
	protected VoxelShape getInsideCollisionShape(BlockState state, BlockView world, BlockPos pos, Entity entity) {
		return shape;
	}
}
