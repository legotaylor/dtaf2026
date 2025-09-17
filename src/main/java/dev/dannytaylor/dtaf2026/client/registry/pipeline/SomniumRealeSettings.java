/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry.pipeline;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.dannytaylor.dtaf2026.client.data.ClientData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.MathHelper;
import org.joml.*;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Environment(EnvType.CLIENT)
public class SomniumRealeSettings implements AutoCloseable {
	private final SomniumRealeStd140Builder builder;
	private final GpuBuffer buffer;
	private int prevLight;
	private int prevSkyLight;
	private int prevBlockLight;
	private float prevBloomAlpha;

	public SomniumRealeSettings() {
		this.builder = new SomniumRealeStd140Builder().putIVec4().putVec4().putVec4();
		this.buffer = RenderSystem.getDevice().createBuffer(() -> "Somnium Reale Settings UBO", 136, this.builder.size());
	}

	public void set(int light, int skyLight, int blockLight, float bloomAlpha) {
		this.builder.set(
			new Vector4i(light, skyLight, blockLight, 0),
			new Vector4f(softSmooth(prevLight, light), softSmooth(prevSkyLight, skyLight), softSmooth(prevBlockLight, blockLight), 0),
			new Vector4f(bloomAlpha, softSmooth(prevBloomAlpha, bloomAlpha), 0, 0)
		);

		try (MemoryStack memoryStack = MemoryStack.stackPush()) {
			RenderSystem.getDevice().createCommandEncoder().writeToBuffer(this.buffer.slice(), this.builder.get(memoryStack));
		}

		prevLight = light;
		prevSkyLight = skyLight;
		prevBlockLight = blockLight;
		prevBloomAlpha = bloomAlpha;
	}

	private float softSmooth(float prev, float current) {
		float softened = (prev + current) * 0.5F;
		return smooth(prev, softened);
	}

	private float smooth(float prevValue, float value) {
		return MathHelper.lerp(ClientData.getMinecraft().getRenderTickCounter().getTickProgress(true), prevValue, value);
	}

	public GpuBuffer get() {
		return this.buffer;
	}

	public void close() {
		this.buffer.close();
	}

	public static class SomniumRealeStd140Builder {
		private final List<Class<?>> layout = new ArrayList<>();
		private Object[] values;

		public SomniumRealeStd140Builder putInt() {
			layout.add(Integer.class);
			return this;
		}

		public SomniumRealeStd140Builder putFloat() {
			layout.add(Float.class);
			return this;
		}

		public SomniumRealeStd140Builder putVec2() {
			layout.add(Vector2fc.class);
			return this;
		}

		public SomniumRealeStd140Builder putIVec2() {
			layout.add(Vector2ic.class);
			return this;
		}

		public SomniumRealeStd140Builder putVec3() {
			layout.add(Vector3fc.class);
			return this;
		}

		public SomniumRealeStd140Builder putIVec3() {
			layout.add(Vector3ic.class);
			return this;
		}

		public SomniumRealeStd140Builder putVec4() {
			layout.add(Vector4fc.class);
			return this;
		}

		public SomniumRealeStd140Builder putIVec4() {
			layout.add(Vector4ic.class);
			return this;
		}

		public void clear() {
			this.values = null;
		}

		public void set(Object... values) {
			clear();
			if (values.length != layout.size()) {
				throw new IllegalArgumentException(
					"Values length (" + values.length + ") does not match layout size (" + layout.size() + ")"
				);
			}
			this.values = values;
		}

		public int size() {
			Std140SizeCalculator builder = new Std140SizeCalculator();
			for (Class<?> type : layout) {
				if (type == Integer.class) builder.putInt();
				else if (type == Float.class) builder.putFloat();
				else if (type == Vector2fc.class) builder.putVec2();
				else if (type == Vector2ic.class) builder.putIVec2();
				else if (type == Vector3fc.class) builder.putVec3();
				else if (type == Vector3ic.class) builder.putIVec3();
				else if (type == Vector4fc.class) builder.putVec4();
				else if (type == Vector4ic.class) builder.putIVec4();
			}
			return builder.get();
		}

		public ByteBuffer get(MemoryStack stack) {
			if (values == null) throw new IllegalStateException("Values not set");
			Std140Builder builder = Std140Builder.onStack(stack, size());
			for (int i = 0; i < layout.size(); i++) {
				Object value = values[i];
				Class<?> type = layout.get(i);
				if (type == Integer.class) builder.putInt((Integer)value);
				else if (type == Float.class) builder.putFloat((Float)value);
				else if (type == Vector2fc.class) builder.putVec2((Vector2fc)value);
				else if (type == Vector2ic.class) builder.putIVec2((Vector2ic)value);
				else if (type == Vector3fc.class) builder.putVec3((Vector3fc)value);
				else if (type == Vector3ic.class) builder.putIVec3((Vector3ic)value);
				else if (type == Vector4fc.class) builder.putVec4((Vector4fc)value);
				else if (type == Vector4ic.class) builder.putIVec4((Vector4ic)value);
				else throw new IllegalStateException("Unsupported type in layout: " + type.getSimpleName());
			}
			return builder.get();
		}
	}
}
