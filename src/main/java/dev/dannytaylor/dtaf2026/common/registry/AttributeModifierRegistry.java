/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry;

import dev.dannytaylor.dtaf2026.common.data.Data;
import net.minecraft.entity.attribute.EntityAttributeModifier;

public class AttributeModifierRegistry {
	public static final EntityAttributeModifier somniumRealeBiomeModifier;

	public static void bootstrap() {
	}

	static {
		somniumRealeBiomeModifier = new EntityAttributeModifier(Data.getSomniumRealeId().withSuffixedPath("_biome"), -0.35F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
	}
}
