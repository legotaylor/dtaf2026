/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.mixin.dimension;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.dannytaylor.dtaf2026.client.data.ClientData;
import dev.dannytaylor.dtaf2026.client.registry.PipelineRegistry;
import dev.dannytaylor.dtaf2026.client.registry.dimension.SomniumRealeEffect;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.BufferAllocator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.joml.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.OptionalDouble;
import java.util.OptionalInt;

@Mixin(SkyRendering.class)
public abstract class SkyRenderingMixin {
	@Unique private GpuBuffer somniumRealeStarVertexBuffer;
	@Unique private int somniumRealeStarIndexCount;
	@Unique private GpuBuffer somniumRealeStarVertexBuffer2;
	@Unique private int somniumRealeStarIndexCount2;
	@Unique private final RenderSystem.ShapeIndexBuffer somniumRealeIndexBuffer = RenderSystem.getSequentialBuffer(VertexFormat.DrawMode.QUADS);
	@Unique private final RenderSystem.ShapeIndexBuffer somniumRealeIndexBuffer2 = RenderSystem.getSequentialBuffer(VertexFormat.DrawMode.QUADS);

	@Inject(method = "<init>", at = @At("RETURN"))
	private void dtaf2026$scaleStars(CallbackInfo ci) {
		this.somniumRealeStarVertexBuffer = this.createSomniumRealeStars(true);
		this.somniumRealeStarVertexBuffer2 = this.createSomniumRealeStars(false);
	}

	@Inject(method = "renderStars", at = @At("RETURN"))
	private void dtaf2026$renderBigStars(float brightness, MatrixStack matrices, CallbackInfo ci) {
		if (this.isSomniumReale()) {
			renderStars(matrices, brightness, true);
			renderStars(matrices, brightness, false);
		}
	}

	@Unique
	private void renderStars(MatrixStack matrices, float brightness, boolean isBig) {
		Matrix4fStack matrix4fStack = RenderSystem.getModelViewStack();
		matrix4fStack.pushMatrix();
		matrix4fStack.mul(matrices.peek().getPositionMatrix());
		RenderPipeline renderPipeline = isBig ? PipelineRegistry.bigStars : PipelineRegistry.tinyStars;
		GpuTextureView gpuTextureView = MinecraftClient.getInstance().getFramebuffer().getColorAttachmentView();
		GpuTextureView gpuTextureView2 = MinecraftClient.getInstance().getFramebuffer().getDepthAttachmentView();
		GpuBuffer gpuBuffer = isBig ? this.somniumRealeIndexBuffer.getIndexBuffer(this.somniumRealeStarIndexCount) : this.somniumRealeIndexBuffer2.getIndexBuffer(this.somniumRealeStarIndexCount2);
		GpuBufferSlice gpuBufferSlice = RenderSystem.getDynamicUniforms().write(matrix4fStack, new Vector4f(brightness, brightness, brightness, brightness), new Vector3f(), new Matrix4f(), 0.0f);
		try (RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "Stars", gpuTextureView, OptionalInt.empty(), gpuTextureView2, OptionalDouble.empty());){
			renderPass.setPipeline(renderPipeline);
			RenderSystem.bindDefaultUniforms(renderPass);
			renderPass.setUniform("DynamicTransforms", gpuBufferSlice);
			renderPass.setVertexBuffer(0, isBig ? this.somniumRealeStarVertexBuffer : this.somniumRealeStarVertexBuffer2);
			renderPass.setIndexBuffer(gpuBuffer, isBig ? this.somniumRealeIndexBuffer.getIndexType() : this.somniumRealeIndexBuffer2.getIndexType());
			renderPass.drawIndexed(0, 0, isBig ? this.somniumRealeStarIndexCount : this.somniumRealeStarIndexCount2, 1);
		}
		matrix4fStack.popMatrix();
	}

	@Redirect(method = "renderStars", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderPass;setPipeline(Lcom/mojang/blaze3d/pipeline/RenderPipeline;)V"))
	private void dtaf2026$updateStarsPipeline(RenderPass instance, RenderPipeline pipeline) {
		updatePipeline(instance, pipeline, PipelineRegistry.stars);
	}

	@Redirect(method = "renderTopSky", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderPass;setPipeline(Lcom/mojang/blaze3d/pipeline/RenderPipeline;)V"))
	private void dtaf2026$updateTopSkyPipeline(RenderPass instance, RenderPipeline pipeline) {
		updatePipeline(instance, pipeline, PipelineRegistry.sky);
	}

	@Redirect(method = "renderSkyDark", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderPass;setPipeline(Lcom/mojang/blaze3d/pipeline/RenderPipeline;)V"))
	private void dtaf2026$updateSkyDarkPipeline(RenderPass instance, RenderPipeline pipeline) {
		updatePipeline(instance, pipeline, PipelineRegistry.sky);
	}

	@Unique
	private void updatePipeline(RenderPass renderPass, RenderPipeline pipeline, RenderPipeline somniumRealePipeline) {
		renderPass.setPipeline(isSomniumReale() ? somniumRealePipeline : pipeline);
	}

	@Unique
	private boolean isSomniumReale() {
		ClientWorld world = ClientData.getMinecraft().world;
		return world != null && world.getDimensionEffects() instanceof SomniumRealeEffect;
	}

	@Unique
	private GpuBuffer createSomniumRealeStars(boolean isBig) {
		Random random = Random.create(isBig ? 5421L : 21684L);
		float f = 100.0F;
		int amount = isBig ? 750 : 2250;
		try (BufferAllocator bufferAllocator = BufferAllocator.method_72201(VertexFormats.POSITION.getVertexSize() * amount * 4)){
			BufferBuilder bufferBuilder = new BufferBuilder(bufferAllocator, VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
			for (int i = 0; i < amount; ++i) {
				float g = random.nextFloat() * 2.0F - 1.0F;
				float h = random.nextFloat() * 2.0F - 1.0F;
				float j = random.nextFloat() * 2.0F - 1.0F;
				float k = (0.15F + random.nextFloat() * 0.1f) * this.getSomniumRealeScale(isBig);
				float l = MathHelper.magnitude(g, h, j);
				if (l <= 0.010000001F || l >= 1.0f) continue;
				Vector3f vector3f = new Vector3f(g, h, j).normalize(f);
				float m = (float)(random.nextDouble() * 3.1415927410125732 * 2.0);
				Matrix3f matrix3f = new Matrix3f().rotateTowards(new Vector3f(vector3f).negate(), new Vector3f(0.0f, 1.0f, 0.0f)).rotateZ(-m);
				bufferBuilder.vertex(new Vector3f(k, -k, 0.0f).mul(matrix3f).add(vector3f));
				bufferBuilder.vertex(new Vector3f(k, k, 0.0f).mul(matrix3f).add(vector3f));
				bufferBuilder.vertex(new Vector3f(-k, k, 0.0f).mul(matrix3f).add(vector3f));
				bufferBuilder.vertex(new Vector3f(-k, -k, 0.0f).mul(matrix3f).add(vector3f));
			}
			try (BuiltBuffer builtBuffer = bufferBuilder.end()) {
				if (isBig) this.somniumRealeStarIndexCount = builtBuffer.getDrawParameters().indexCount();
				else this.somniumRealeStarIndexCount2 = builtBuffer.getDrawParameters().indexCount();
				GpuBuffer gpuBuffer = RenderSystem.getDevice().createBuffer(() -> "Stars vertex buffer", 40, builtBuffer.getBuffer());
				builtBuffer.close();
				return gpuBuffer;
			}
		}
	}

	@Unique
	private float getSomniumRealeScale(boolean isBig) {
		return isBig ? 2.0F : 0.5F;
	}
}
