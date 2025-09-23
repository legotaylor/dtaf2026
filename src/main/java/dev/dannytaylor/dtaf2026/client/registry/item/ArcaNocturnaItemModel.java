/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry.item;

import com.mojang.serialization.MapCodec;
import dev.dannytaylor.dtaf2026.common.registry.item.ArcaNocturnaItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.model.ResolvableModel;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class ArcaNocturnaItemModel implements ItemModel {
	public static final ItemModel INSTANCE = new ArcaNocturnaItemModel();

	public void update(ItemRenderState state, ItemStack stack, ItemModelManager resolver, ItemDisplayContext displayContext, @Nullable ClientWorld world, @Nullable LivingEntity user, int seed) {
		state.addModelKey(this);
		ItemStack itemStack = ArcaNocturnaItem.getContents(stack).getStack();
		if (!itemStack.isEmpty()) {
			resolver.update(state, itemStack, displayContext, world, user, seed);
		}

	}

	@Environment(EnvType.CLIENT)
	public static record Unbaked() implements ItemModel.Unbaked {
		public static final MapCodec<ArcaNocturnaItemModel.Unbaked> CODEC = MapCodec.unit(new ArcaNocturnaItemModel.Unbaked());

		public MapCodec<ArcaNocturnaItemModel.Unbaked> getCodec() {
			return CODEC;
		}

		public ItemModel bake(ItemModel.BakeContext context) {
			return ArcaNocturnaItemModel.INSTANCE;
		}

		public void resolve(ResolvableModel.Resolver resolver) {
		}
	}
}

