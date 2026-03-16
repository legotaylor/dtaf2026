/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.Optional;

public class FleeciferFightCriterion extends AbstractCriterion<FleeciferFightCriterion.Conditions> {
	@Override
	public Codec<FleeciferFightCriterion.Conditions> getConditionsCodec() {
		return FleeciferFightCriterion.Conditions.codec;
	}

	public void trigger(ServerPlayerEntity player, boolean finished, int phase) {
		ServerWorld serverWorld = player.getWorld();
		LootWorldContext lootWorldContext = new LootWorldContext.Builder(serverWorld)
			.add(LootContextParameters.THIS_ENTITY, player)
			.add(LootContextParameterRegistry.fleeciferFightFinished, finished)
			.add(LootContextParameterRegistry.fleeciferBossPhase, phase)
			.build(LootContextTypeRegistry.fleeciferFight);
		LootContext lootContext = new LootContext.Builder(lootWorldContext).build(Optional.empty());
		this.trigger(player, conditions -> conditions.test(lootContext));
	}

	public record Conditions(Optional<LootContextPredicate> player,
							 Optional<LootContextPredicate> fight) implements AbstractCriterion.Conditions {
		public static final Codec<FleeciferFightCriterion.Conditions> codec;

		static {
			codec = RecordCodecBuilder.create(instance -> instance.group(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player").forGetter(FleeciferFightCriterion.Conditions::player), EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("fight").forGetter(FleeciferFightCriterion.Conditions::fight)).apply(instance, FleeciferFightCriterion.Conditions::new));
		}

		public boolean test(LootContext lootContext) {
			return this.fight.isEmpty() || this.fight.get().test(lootContext);
		}
	}
}
