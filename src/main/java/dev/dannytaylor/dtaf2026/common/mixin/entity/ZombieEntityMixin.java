/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.mixin.entity;

import dev.dannytaylor.dtaf2026.common.registry.block.BlockRegistry;
import dev.dannytaylor.dtaf2026.common.registry.entity.goal.DestroyJunglefowlEggGoal;
import dev.dannytaylor.dtaf2026.common.registry.item.ItemRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ZombieEntity.class)
public abstract class ZombieEntityMixin extends HostileEntity {
	protected ZombieEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "initGoals", at = @At("RETURN"))
	public void dtaf2026$initGoals(CallbackInfo ci) {
		this.goalSelector.add(4, new DestroyJunglefowlEggGoal(BlockRegistry.redEgg, ItemRegistry.redEgg, this, 1.0, 3, 1.5));
		this.goalSelector.add(4, new DestroyJunglefowlEggGoal(BlockRegistry.largeRedEgg, ItemRegistry.largeRedEgg, this, 1.0, 3, 1.0));
		this.goalSelector.add(4, new DestroyJunglefowlEggGoal(BlockRegistry.grayEgg, ItemRegistry.grayEgg, this, 1.0, 3, 1.5));
		this.goalSelector.add(4, new DestroyJunglefowlEggGoal(BlockRegistry.largeGrayEgg, ItemRegistry.largeGrayEgg, this, 1.0, 3, 1.0));
	}
}
