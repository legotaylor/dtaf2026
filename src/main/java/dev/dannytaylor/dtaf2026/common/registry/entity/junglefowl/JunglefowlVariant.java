/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.entity.junglefowl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.VariantSelectorProvider;
import net.minecraft.entity.spawn.SpawnCondition;
import net.minecraft.entity.spawn.SpawnConditionSelectors;
import net.minecraft.entity.spawn.SpawnContext;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.AssetInfo;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record JunglefowlVariant(ModelAndTextureAndTexture<JunglefowlVariant.Model> modelAndTextureAndTexture,
								SpawnConditionSelectors spawnConditions) implements VariantSelectorProvider<SpawnContext, SpawnCondition> {
	public static final Codec<JunglefowlVariant> codec;
	public static final Codec<JunglefowlVariant> networkCodec;
	public static final PacketCodec<RegistryByteBuf, JunglefowlVariant> packetCodec;

	static {
		codec = RecordCodecBuilder.create((instance) -> instance.group(ModelAndTextureAndTexture.createMapCodec(JunglefowlVariant.Model.codec, JunglefowlVariant.Model.big).forGetter(JunglefowlVariant::modelAndTextureAndTexture), SpawnConditionSelectors.CODEC.fieldOf("spawn_conditions").forGetter(JunglefowlVariant::spawnConditions)).apply(instance, JunglefowlVariant::new));
		networkCodec = RecordCodecBuilder.create((instance) -> instance.group(ModelAndTextureAndTexture.createMapCodec(JunglefowlVariant.Model.codec, JunglefowlVariant.Model.big).forGetter(JunglefowlVariant::modelAndTextureAndTexture)).apply(instance, JunglefowlVariant::new));
		packetCodec = ModelAndTextureAndTexture.createPacketCodec(Model.packetCodec).xmap(JunglefowlVariant::new, JunglefowlVariant::modelAndTextureAndTexture);
	}

	private JunglefowlVariant(ModelAndTextureAndTexture<JunglefowlVariant.Model> modelAndTextureAndTexture) {
		this(modelAndTextureAndTexture, SpawnConditionSelectors.EMPTY);
	}

	public List<Selector<SpawnContext, SpawnCondition>> getSelectors() {
		return this.spawnConditions.selectors();
	}

	public @NotNull String toString() {
		return "JunglefowlVariant[modelAndTexture=" + modelAndTextureAndTexture() + ", spawnConditions=" + spawnConditions() + "]";
	}

	public enum Model implements StringIdentifiable {
		big("big"),
		small("small");

		public static final Codec<JunglefowlVariant.Model> codec = StringIdentifiable.createCodec(JunglefowlVariant.Model::values);
		public static final PacketCodec<ByteBuf, Model> packetCodec = PacketCodecs.STRING.xmap(JunglefowlVariant.Model::valueOf, JunglefowlVariant.Model::asString);

		private final String id;

		Model(final String id) {
			this.id = id;
		}

		public String asString() {
			return this.id;
		}
	}

	public record ModelAndTextureAndTexture<T>(T model, AssetInfo asset, AssetInfo babyAsset) {
		public ModelAndTextureAndTexture(T model, Identifier assetId, Identifier babyAssetId) {
			this(model, new AssetInfo(assetId), new AssetInfo(babyAssetId));
		}

		public static <T> MapCodec<ModelAndTextureAndTexture<T>> createMapCodec(Codec<T> modelCodec, T model) {
			return RecordCodecBuilder.mapCodec(instance -> instance.group(modelCodec.optionalFieldOf("model", model).forGetter(ModelAndTextureAndTexture::model), AssetInfo.CODEC.fieldOf("asset_id").forGetter(ModelAndTextureAndTexture::asset), AssetInfo.CODEC.fieldOf("baby_asset_id").forGetter(ModelAndTextureAndTexture::babyAsset)).apply(instance, ModelAndTextureAndTexture::new));
		}

		public static <T> PacketCodec<RegistryByteBuf, ModelAndTextureAndTexture<T>> createPacketCodec(PacketCodec<? super RegistryByteBuf, T> modelPacketCodec) {
			return PacketCodec.tuple(modelPacketCodec, ModelAndTextureAndTexture::model, AssetInfo.PACKET_CODEC, ModelAndTextureAndTexture::asset, AssetInfo.PACKET_CODEC, ModelAndTextureAndTexture::babyAsset, ModelAndTextureAndTexture::new);
		}

		public @NotNull String toString() {
			return "ModelAndTextureAndTexture[model=" + model() + ", asset=" + asset() + ", babyAsset=" + babyAsset() + "]";
		}
	}
}

