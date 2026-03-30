/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.item;

import dev.dannytaylor.dtaf2026.client.data.ClientData;
import dev.dannytaylor.dtaf2026.common.data.Data;
import dev.dannytaylor.dtaf2026.common.registry.entity.fleecifer.FleeciferEyeEntity;
import dev.dannytaylor.dtaf2026.common.registry.sound.SoundEventRegistry;
import dev.dannytaylor.dtaf2026.common.registry.tagkey.TagRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class FleeciferEyeItem extends Item {
	public FleeciferEyeItem(Settings settings) {
		super(settings);
	}

	@Override
	public int getMaxUseTime(ItemStack stack, LivingEntity user) {
		return 0;
	}

	@Override
	public ActionResult use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		user.setCurrentHand(hand);
		if (world instanceof ServerWorld) {
			if (!world.getBiome(user.getBlockPos()).isIn(TagRegistry.WorldGen.Biome.theTerrorlands)) {
				user.sendMessage(Data.getText("fleecifer_eye.not_in_biome").formatted(Formatting.RED), true);
				return ActionResult.PASS;
			}
			BlockPos blockPos = BlockPos.ofFloored(user.getPos().add(user.getRotationVec(1.0F).multiply(48)).add(0, 32, 0));
			FleeciferEyeEntity entity = FleeciferEyeEntity.create(world, user.getX(), user.getBodyY(0.5), user.getZ());
			entity.setItem(itemStack);
			entity.initTargetPos(Vec3d.of(blockPos));
			world.emitGameEvent(GameEvent.PROJECTILE_SHOOT, entity.getPos(), GameEvent.Emitter.of(user));
			world.spawnEntity(entity);

			world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEventRegistry.summonFleeciferBoss, SoundCategory.NEUTRAL, 1.0F, 1.0F);
			itemStack.decrementUnlessCreative(1, user);
			user.incrementStat(Stats.USED.getOrCreateStat(this));
		}
		user.getItemCooldownManager().set(itemStack, 100);
		return ActionResult.SUCCESS_SERVER;
	}
}
