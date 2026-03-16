/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.context.ContextParameter;

import java.util.Set;

public record FleeciferFightFinishedCondition(boolean finished) implements LootCondition {
	public static final MapCodec<FleeciferFightFinishedCondition> CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(Codec.BOOL.fieldOf("finished").forGetter(FleeciferFightFinishedCondition::finished))
			.apply(instance, FleeciferFightFinishedCondition::new)
	);

	public boolean test(LootContext lootContext) {
		return this.finished == Boolean.TRUE.equals(lootContext.get(LootContextParameterRegistry.fleeciferFightFinished));
	}

	@Override
	public LootConditionType getType() {
		return LootConditionTypeRegistry.fleeciferFightFinished;
	}

	@Override
	public Set<ContextParameter<?>> getAllowedParameters() {
		return Set.of(LootContextParameterRegistry.fleeciferFightFinished);
	}
}
