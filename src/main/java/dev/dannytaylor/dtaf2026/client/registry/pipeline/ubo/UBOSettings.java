/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry.pipeline.ubo;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.*;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public abstract class UBOSettings implements AutoCloseable {
	public final String name;
	public final Std140Builder builder;
	private final GpuBuffer buffer;

	public UBOSettings(String name, Std140Builder builder) {
		this.name = name;
		this.builder = builder;
		this.buffer = RenderSystem.getDevice().createBuffer(() -> name + " Settings UBO", 136, this.builder.size());
	}

	public void set(Object... values) {
		this.builder.set(values);
	}

	public void apply() {
		try (MemoryStack memoryStack = MemoryStack.stackPush()) {
			RenderSystem.getDevice().createCommandEncoder().writeToBuffer(this.buffer.slice(), this.builder.get(memoryStack));
		}
	}

	public GpuBuffer get() {
		return this.buffer;
	}

	public void close() {
		this.buffer.close();
	}

	public static class Std140Builder {
		private final List<Class<?>> layout = new ArrayList<>();
		private Object[] values;

		public Std140Builder putInt() {
			layout.add(Integer.class);
			return this;
		}

		public Std140Builder putFloat() {
			layout.add(Float.class);
			return this;
		}

		public Std140Builder putVec2() {
			layout.add(Vector2fc.class);
			return this;
		}

		public Std140Builder putIVec2() {
			layout.add(Vector2ic.class);
			return this;
		}

		public Std140Builder putVec3() {
			layout.add(Vector3fc.class);
			return this;
		}

		public Std140Builder putIVec3() {
			layout.add(Vector3ic.class);
			return this;
		}

		public Std140Builder putVec4() {
			layout.add(Vector4fc.class);
			return this;
		}

		public Std140Builder putIVec4() {
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
			com.mojang.blaze3d.buffers.Std140Builder builder = com.mojang.blaze3d.buffers.Std140Builder.onStack(stack, size());
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
