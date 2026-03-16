/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.entity.goal;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.goal.MoveToTargetPosGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;

public class StepAndDestroyBlockGoal extends MoveToTargetPosGoal {
	private final TagKey<Block> targetBlockTag;
	private final Block targetBlock;
	private final Item breakParticleItem;
	private final MobEntity stepAndDestroyMob;
	private int counter;

	public StepAndDestroyBlockGoal(TagKey<Block> targetBlockTag, Item breakParticleItem, PathAwareEntity mob, double speed, int maxYDifference) {
		super(mob, speed, 24, maxYDifference);
		this.targetBlockTag = targetBlockTag;
		this.targetBlock = null;
		this.breakParticleItem = breakParticleItem;
		this.stepAndDestroyMob = mob;
	}

	public StepAndDestroyBlockGoal(Block targetBlock, Item breakParticleItem, PathAwareEntity mob, double speed, int maxYDifference) {
		super(mob, speed, 24, maxYDifference);
		this.targetBlockTag = null;
		this.targetBlock = targetBlock;
		this.breakParticleItem = breakParticleItem;
		this.stepAndDestroyMob = mob;
	}

	public boolean canStart() {
		if (!StepAndDestroyBlockGoal.getServerWorld(this.stepAndDestroyMob).getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
			return false;
		}
		if (this.cooldown > 0) {
			--this.cooldown;
			return false;
		}
		if (this.findTargetPos()) {
			this.cooldown = StepAndDestroyBlockGoal.toGoalTicks(20);
			return true;
		}
		this.cooldown = this.getInterval(this.mob);
		return false;
	}

	public void stop() {
		super.stop();
		this.stepAndDestroyMob.fallDistance = 1.0;
	}

	public void start() {
		super.start();
		this.counter = 0;
	}

	public void tickStepping(WorldAccess world, BlockPos pos) {
	}

	public void onDestroyBlock(World world, BlockPos pos) {
	}

	public void tick() {
		super.tick();
		World world = this.stepAndDestroyMob.getWorld();
		BlockPos blockPos = this.stepAndDestroyMob.getBlockPos();
		BlockPos blockPos2 = this.tweakToProperPos(blockPos, world);
		Random random = this.stepAndDestroyMob.getRandom();
		if (this.hasReached() && blockPos2 != null) {
			Vec3d vec3d;
			if (this.counter > 0) {
				vec3d = this.stepAndDestroyMob.getVelocity();
				this.stepAndDestroyMob.setVelocity(vec3d.x, 0.3, vec3d.z);
				if (!world.isClient)
					((ServerWorld) world).spawnParticles(new ItemStackParticleEffect(ParticleTypes.ITEM, new ItemStack(this.breakParticleItem)), (double) blockPos2.getX() + 0.5, (double) blockPos2.getY() + 0.7, (double) blockPos2.getZ() + 0.5, 3, ((double) random.nextFloat() - 0.5) * 0.08, ((double) random.nextFloat() - 0.5) * 0.08, ((double) random.nextFloat() - 0.5) * 0.08, 0.15f);
			}
			if (this.counter % 2 == 0) {
				vec3d = this.stepAndDestroyMob.getVelocity();
				this.stepAndDestroyMob.setVelocity(vec3d.x, -0.3, vec3d.z);
				if (this.counter % 6 == 0) {
					this.tickStepping(world, this.targetPos);
				}
			}
			if (this.counter > 60) {
				world.removeBlock(blockPos2, false);
				if (!world.isClient) {
					for (int i = 0; i < 20; ++i)
						((ServerWorld) world).spawnParticles(ParticleTypes.POOF, (double) blockPos2.getX() + 0.5, blockPos2.getY(), (double) blockPos2.getZ() + 0.5, 1, random.nextGaussian() * 0.02, random.nextGaussian() * 0.02, random.nextGaussian() * 0.02, 0.15f);
					this.onDestroyBlock(world, blockPos2);
				}
			}
			++this.counter;
		}
	}

	private BlockPos tweakToProperPos(BlockPos pos, BlockView world) {
		if (isTargetBlock(world.getBlockState(pos))) return pos;
		for (BlockPos blockPos : new BlockPos[]{pos.down(), pos.west(), pos.east(), pos.north(), pos.south(), pos.down().down()}) {
			if (!isTargetBlock(world.getBlockState(blockPos))) continue;
			return blockPos;
		}
		return null;
	}

	protected boolean isTargetPos(WorldView world, BlockPos pos) {
		Chunk chunk = world.getChunk(ChunkSectionPos.getSectionCoord(pos.getX()), ChunkSectionPos.getSectionCoord(pos.getZ()), ChunkStatus.FULL, false);
		if (chunk != null)
			return isTargetBlock(chunk.getBlockState(pos)) && chunk.getBlockState(pos.up()).isAir() && chunk.getBlockState(pos.up(2)).isAir();
		return false;
	}

	public boolean isTargetBlock(BlockState state) {
		boolean block = false;
		boolean tag = false;
		if (this.targetBlock != null) block = state.isOf(this.targetBlock);
		if (this.targetBlockTag != null) tag = state.isIn(this.targetBlockTag);
		return block || tag;
	}
}
