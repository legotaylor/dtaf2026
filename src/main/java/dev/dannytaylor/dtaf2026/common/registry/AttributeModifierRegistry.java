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
	public static final EntityAttributeModifier somniumRealeScaleModifier;
	public static final EntityAttributeModifier growthEffectModifier;
	public static final EntityAttributeModifier shrinkEffectModifier;

	public static void bootstrap() {
	}

	static {
		somniumRealeScaleModifier = new EntityAttributeModifier(Data.getSomniumRealeId().withSuffixedPath("_scale"), -0.35F, EntityAttributeModifier.Operation.ADD_VALUE);
		growthEffectModifier = new EntityAttributeModifier(Data.idOf("growth"), 0.35F, EntityAttributeModifier.Operation.ADD_VALUE);
		shrinkEffectModifier = new EntityAttributeModifier(Data.idOf("shrink"), -0.35F, EntityAttributeModifier.Operation.ADD_VALUE);
	}
}
