/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.entity.goal;

import net.minecraft.block.Block;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class DestroyJunglefowlEggGoal extends StepAndDestroyBlockGoal {
	private final double desiredDistance;

	public DestroyJunglefowlEggGoal(TagKey<Block> block, Item item, PathAwareEntity mob, double speed, int maxYDifference, double desiredDistance) {
		super(block, item, mob, speed, maxYDifference);
		this.desiredDistance = desiredDistance;
	}

	public DestroyJunglefowlEggGoal(Block block, Item item, PathAwareEntity mob, double speed, int maxYDifference, double desiredDistance) {
		super(block, item, mob, speed, maxYDifference);
		this.desiredDistance = desiredDistance;
	}

	public void tickStepping(WorldAccess world, BlockPos pos) {
		world.playSound(null, pos, SoundEvents.ENTITY_ZOMBIE_DESTROY_EGG, SoundCategory.HOSTILE, 0.5f, 0.9f + world.getRandom().nextFloat() * 0.2f);
	}

	public void onDestroyBlock(World world, BlockPos pos) {
		world.playSound(null, pos, SoundEvents.ENTITY_TURTLE_EGG_BREAK, SoundCategory.BLOCKS, 0.7f, 0.9f + world.random.nextFloat() * 0.2f);
	}

	public double getDesiredDistanceToTarget() {
		return this.desiredDistance;
	}
}
