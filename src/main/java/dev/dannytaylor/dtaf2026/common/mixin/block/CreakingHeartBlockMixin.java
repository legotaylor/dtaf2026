/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.mixin.block;

import dev.dannytaylor.dtaf2026.common.registry.block.BlockRegistry;
import dev.dannytaylor.dtaf2026.common.registry.block.CreakingVariant;
import dev.dannytaylor.dtaf2026.common.registry.item.ItemRegistry;
import dev.dannytaylor.dtaf2026.common.registry.tagkey.TagRegistry;
import dev.dannytaylor.dtaf2026.common.registry.worldgen.DimensionRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CreakingHeartBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CreakingHeartBlock.class)
public abstract class CreakingHeartBlockMixin extends Block {
	public CreakingHeartBlockMixin(Settings settings) {
		super(settings);
	}

	@Inject(method = "isNightAndNatural", at = @At("RETURN"), cancellable = true)
	private static void dtaf2026$isNightAndNatural(World world, CallbackInfoReturnable<Boolean> cir) {
		if (DimensionRegistry.isAlwaysNight.contains(world.getDimension().effects())) cir.setReturnValue(true);
	}

	@ModifyArg(method = "isSurroundedByPaleOakLogs", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
	private static TagKey<Block> dtaf2026$isSurroundedByLogs(TagKey<Block> par1) {
		return TagRegistry.Block.isCreakingHeartLog;
	}

	@ModifyArg(method = "shouldBeEnabled", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
	private static TagKey<Block> dtaf2026$shouldBeEnabled(TagKey<Block> par1) {
		return TagRegistry.Block.isCreakingHeartLog;
	}

	@Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/CreakingHeartBlock;setDefaultState(Lnet/minecraft/block/BlockState;)V"))
	private void dtaf2026$setDefaultState(CreakingHeartBlock instance, BlockState state) {
		this.setDefaultState(state.with(BlockRegistry.Properties.creakingVariant, CreakingVariant.VANILLA));
	}

	@Inject(method = "appendProperties", at = @At("RETURN"))
	private void dtaf2026$appendProperties(StateManager.Builder<Block, BlockState> builder, CallbackInfo ci) {
		builder.add(BlockRegistry.Properties.creakingVariant);
	}

	@Override
	protected ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state, boolean includeData) {
		return new ItemStack(switch (state.get(BlockRegistry.Properties.creakingVariant)) {
			case MAPLE -> ItemRegistry.mapleCreakingHeart;
			case CERULEAN -> ItemRegistry.ceruleanCreakingHeart;
			case VANILLA -> Items.CREAKING_HEART;
		});
	}
}
