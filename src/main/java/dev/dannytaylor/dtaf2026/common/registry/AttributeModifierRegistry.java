/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry;

import dev.dannytaylor.dtaf2026.common.data.Data;
import net.minecraft.entity.attribute.EntityAttributeModifier;

public class AttributeModifierRegistry {
	public static final EntityAttributeModifier somniumRealeScale;
	public static final EntityAttributeModifier growth;
	public static final EntityAttributeModifier shrink;

	public static void bootstrap() {
	}

	static {
		somniumRealeScale = new EntityAttributeModifier(Data.getSomniumRealeId().withSuffixedPath("_scale"), -0.35F, EntityAttributeModifier.Operation.ADD_VALUE);
		growth = new EntityAttributeModifier(Data.idOf("growth"), 0.35F, EntityAttributeModifier.Operation.ADD_VALUE);
		shrink = new EntityAttributeModifier(Data.idOf("shrink"), -0.35F, EntityAttributeModifier.Operation.ADD_VALUE);
	}
}
