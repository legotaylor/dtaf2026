/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.mixin.block;

import dev.dannytaylor.dtaf2026.common.registry.BlockRegistry;
import dev.dannytaylor.dtaf2026.common.registry.entity.creaking.TerrorlandsCreaking;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.CreakingHeartBlockEntity;
import net.minecraft.entity.mob.CreakingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CreakingHeartBlockEntity.class)
public abstract class CreakingHeartBlockEntityMixin {
	@Inject(method = "spawnCreakingPuppet", at = @At("RETURN"), cancellable = true)
	private static void dtaf2026$spawnPuppet(ServerWorld world, CreakingHeartBlockEntity blockEntity, CallbackInfoReturnable<CreakingEntity> cir) {
		TerrorlandsCreaking creakingEntity = (TerrorlandsCreaking) cir.getReturnValue();
		if (creakingEntity != null) {
			creakingEntity.dtaf2026$setVariant(world.getBlockState(blockEntity.getPos()).get(BlockRegistry.Properties.creakingVariant).variant);
			cir.setReturnValue((CreakingEntity) creakingEntity);
		}
	}

	@Inject(method = "tick", at = @At("RETURN"))
	private static void dtaf2026$tick(World world, BlockPos pos, BlockState state, CreakingHeartBlockEntity blockEntity, CallbackInfo ci) {
		// If state is changed via debug stick, change the creaking variant.
		if (world instanceof ServerWorld serverWorld) {
			Identifier variant = serverWorld.getBlockState(blockEntity.getPos()).get(BlockRegistry.Properties.creakingVariant).variant;
			blockEntity.getCreakingPuppet().ifPresent((puppet) -> {
				if (!((TerrorlandsCreaking) puppet).dtaf2026$getVariant().equals(variant))
					((TerrorlandsCreaking) puppet).dtaf2026$setVariant(variant);
			});
		}
	}
}
