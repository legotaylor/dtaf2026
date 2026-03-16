/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.block;

import com.mojang.serialization.Codec;
import dev.dannytaylor.dtaf2026.common.data.Data;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;

public enum CreakingVariant implements StringIdentifiable {
	MAPLE(Data.idOf("maple")),
	CERULEAN(Data.idOf("cerulean")),
	VANILLA(Identifier.of("creaking"));

	public static final Codec<CreakingVariant> codec = StringIdentifiable.createCodec(CreakingVariant::values);

	public final Identifier variant;

	CreakingVariant(Identifier identifier) {
		this.variant = identifier;
	}

	@Override
	public String asString() {
		return this.variant.toUnderscoreSeparatedString();
	}

	public static CreakingVariant fromIdentifier(Identifier identifier) {
		for (CreakingVariant value : values()) {
			if (value.variant.equals(identifier)) return value;
		}
		return null;
	}
}
