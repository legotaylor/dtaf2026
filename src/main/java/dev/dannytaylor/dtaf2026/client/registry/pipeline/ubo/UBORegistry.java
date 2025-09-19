/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry.pipeline.ubo;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.systems.RenderPass;
import dev.dannytaylor.dtaf2026.client.config.Config;
import dev.dannytaylor.dtaf2026.client.data.ClientData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;

import java.util.ArrayList;
import java.util.List;

public class UBORegistry {
	private static final List<UBO<?>> registry = new ArrayList<>();

	public static void bootstrap() {
		registry.add(new UBO<>(new SomniumRealeUBO("SomniumReale"), SomniumRealeUBO::set, (client, settings) -> {
			int light = 15;
			int skyLight = 15;
			int blockLight = 15;
			if (ClientData.getMinecraft().player != null && ClientData.getMinecraft().world != null) {
				BlockPos blockPos = ClientData.getMinecraft().player.getBlockPos();
				light = ClientData.getMinecraft().world.getChunkManager().getLightingProvider().getLight(blockPos, 0);
				skyLight = ClientData.getMinecraft().world.getLightLevel(LightType.SKY, blockPos);
				blockLight = ClientData.getMinecraft().world.getLightLevel(LightType.BLOCK, blockPos);
			}
			settings.update(light, skyLight, blockLight, Config.instance.bloomAlpha.value(), ClientData.getMinecraft().world == null ? 0L : ClientData.getMinecraft().world.getTime());
		}));
	}

	public static void bindDefaultUniforms(RenderPass pass) {
		for (UBO<?> ubo : registry) ubo.bind(pass);
	}

	private static void bindUniform(RenderPass pass, GpuBuffer settings, String name) {
		pass.setUniform(name, settings);
	}

	public static void apply() {
		for (UBO<?> ubo : registry) ubo.apply();
	}

	public static void tick(MinecraftClient client) {
		for (UBO<?> ubo : registry) ubo.tick(client);
	}

	public static void close() {
		for (UBO<?> ubo : registry) ubo.close();
	}

	public static class UBO<T extends UBOSettings> {
		private final T settings;
		private final Apply<T> onApply;
		private final Tick<T> tick;

		public UBO(T settings) {
			this(settings, (uboSettings) -> {}, (client, uboSettings) -> {});
		}

		public UBO(T settings, Apply<T> onApply, Tick<T> tick) {
			this.onApply = onApply;
			this.settings = settings;
			this.tick = tick;
		}

		public void bind(RenderPass pass) {
			bindUniform(pass, this.settings.get(), this.settings.name);
		}

		public void apply() {
			this.onApply.apply(this.settings);
			this.settings.apply();
		}

		public void tick(MinecraftClient client) {
			this.tick.tick(client, this.settings);
		}

		public void close() {
			this.settings.close();
		}

		public interface Apply<T extends UBOSettings> {
			void apply(T settings);
		}

		public interface Tick<T extends UBOSettings> {
			void tick(MinecraftClient client, T settings);
		}
	}
}
