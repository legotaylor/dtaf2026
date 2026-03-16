/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.config.value;

import folk.sisby.kaleido.lib.quiltconfig.api.values.ConfigSerializableObject;
import net.minecraft.util.function.ValueLists;

import java.util.function.IntFunction;

public enum EffectType implements ConfigSerializableObject<String> {
	full(0, "full"),
	reduced(1, "reduced"),
	none(2, "none");

	private static final IntFunction<EffectType> BY_ID = ValueLists.createIndexToValueFunction(EffectType::getNumericId, values(), ValueLists.OutOfBoundsHandling.WRAP);

	private final int numericId;
	private final String id;

	EffectType(int numericId, String id) {
		this.numericId = numericId;
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public int getNumericId() {
		return this.numericId;
	}

	public EffectType convertFrom(String representation) {
		return valueOf(representation);
	}

	public String getRepresentation() {
		return this.name();
	}

	public EffectType copy() {
		return this;
	}

	public static EffectType byId(int id) {
		return BY_ID.apply(id);
	}
}
