/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.Map;

// This class is only for the client as letting UNSAFE on servers could be an issue.
// I wish they used records instead :sob:
@Environment(EnvType.CLIENT)
public class UnsafeEnum {
	public static final Unsafe UNSAFE;

	public static <T extends Enum<?>> T createEnumInstance(Class<T> enumClass, String name, int ordinal, Map<String, Object> extraFields) {
		try {
			T instance = (T) UNSAFE.allocateInstance(enumClass);
			UNSAFE.putObject(instance, UNSAFE.objectFieldOffset(Enum.class.getDeclaredField("name")), name);
			UNSAFE.putInt(instance, UNSAFE.objectFieldOffset(Enum.class.getDeclaredField("ordinal")), ordinal);
			if (extraFields != null) {
				for (Map.Entry<String, Object> entry : extraFields.entrySet()) {
					UNSAFE.putObject(instance, UNSAFE.objectFieldOffset(enumClass.getDeclaredField(entry.getKey())), entry.getValue());
				}
			}
			return instance;
		} catch (Exception error) {
			throw new RuntimeException("Failed to create enum instance", error);
		}
	}

	static {
		try {
			Field f = Unsafe.class.getDeclaredField("theUnsafe");
			f.setAccessible(true);
			UNSAFE = (Unsafe) f.get(null);
		} catch (Exception error) {
			throw new RuntimeException("Failed to get Unsafe", error);
		}
	}
}
