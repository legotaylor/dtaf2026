/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.mixin.entity;

import dev.dannytaylor.dtaf2026.common.registry.block.BlockRegistry;
import dev.dannytaylor.dtaf2026.common.registry.block.CreakingVariant;
import dev.dannytaylor.dtaf2026.common.registry.entity.TrackedDataRegistry;
import dev.dannytaylor.dtaf2026.common.registry.entity.creaking.TerrorlandsCreaking;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.mob.CreakingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(CreakingEntity.class)
public abstract class CreakingEntityMixin extends HostileEntity implements TerrorlandsCreaking {
	@Unique
	private static final Identifier defaultId = Identifier.of("creaking");
	@Unique
	private static final TrackedData<Identifier> variant;

	static {
		variant = DataTracker.registerData(CreakingEntityMixin.class, TrackedDataRegistry.variant);
	}

	protected CreakingEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "initDataTracker", at = @At("RETURN"))
	private void dtaf2026$initDataTracker(DataTracker.Builder builder, CallbackInfo ci) {
		builder.add(variant, defaultId);
	}

	@ModifyArgs(method = "finishCrumbling", at = @At(value = "INVOKE", target = "Lnet/minecraft/particle/BlockStateParticleEffect;<init>(Lnet/minecraft/particle/ParticleType;Lnet/minecraft/block/BlockState;)V"))
	private void dtaf2026$finishCrumbling(Args args) {
		CreakingVariant variant = CreakingVariant.fromIdentifier(this.dtaf2026$getVariant());
		if (variant != null) {
			if (args.get(1).equals(Blocks.PALE_OAK_WOOD.getDefaultState())) {
				args.set(1, switch (variant) {
					case MAPLE -> BlockRegistry.maple.wood.getDefaultState();
					case CERULEAN -> BlockRegistry.cerulean.wood.getDefaultState();
					case VANILLA -> (BlockState) args.get(1);
				});
			} else args.set(1, ((BlockState) args.get(1)).with(BlockRegistry.Properties.creakingVariant, variant));
		}
	}

	@Override
	public Identifier dtaf2026$getVariant() {
		return this.dataTracker.get(variant);
	}

	@Override
	public void dtaf2026$setVariant(Identifier value) {
		this.dataTracker.set(variant, value);
	}

	public void readCustomData(ReadView view) {
		super.readCustomData(view);
		this.dtaf2026$setVariant(Identifier.of(view.getString("Variant", defaultId.toString())));
	}

	public void writeCustomData(WriteView view) {
		super.writeCustomData(view);
		view.putString("Variant", this.dtaf2026$getVariant().toString());
	}
}
