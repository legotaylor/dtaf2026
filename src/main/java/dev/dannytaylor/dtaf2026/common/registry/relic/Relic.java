/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.relic;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.dannytaylor.dtaf2026.common.data.Data;
import dev.dannytaylor.dtaf2026.common.registry.TagRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class Relic {
	private boolean active;
	public RelicAction action;
	public List<RelicCondition> conditions;

	public Relic(RelicAction action, List<RelicCondition> conditions) {
		this.action = action;
		this.conditions = conditions;
	}

	public void tick(LivingEntity livingEntity) {
		if (testConditions(conditions, livingEntity)) {
			this.action.apply(livingEntity, this.active);
			if (!this.active) this.active = true;
		} else {
			if (this.active) this.active = false;
		}
	}

	public JsonObject toJson() {
		JsonObject json = action.toJson();
		if (!conditions.isEmpty()) {
			JsonArray array = new JsonArray();
			for (RelicCondition condition : conditions) array.add(condition.toJson());
			json.add("conditions", array);
		}
		return json;
	}

	public interface RelicAction {
		void apply(LivingEntity entity, boolean active);

		JsonObject toJson();

		Map<Identifier, Function<JsonElement, RelicAction>> registry = new HashMap<>();

		static void register(Identifier type, Function<JsonElement, RelicAction> parser) {
			registry.put(type, parser);
		}

		static RelicAction fromJson(JsonObject json) {
			Identifier type = Identifier.of(json.get("type").getAsString());
			JsonElement value = json.has("value") ? json.get("value") : new JsonObject();
			Function<JsonElement, RelicAction> parser = registry.get(type);
			if (parser == null) throw new IllegalArgumentException("Unknown action type: " + type);
			return parser.apply(value);
		}
	}

	public record StatusEffectAction(List<StatusEffectInstanceData> effects) implements RelicAction {
		public JsonObject toJson() {
			JsonObject json = new JsonObject();
			json.addProperty("type", ActionTypes.statusEffect.toString());
			JsonArray array = new JsonArray();
			for (StatusEffectInstanceData effect : effects) array.add(effect.toJson());
			json.add("value", array);
			return json;
		}

		public void apply(LivingEntity entity, boolean active) {
			entity.getWorld().getRegistryManager().getOptional(RegistryKeys.STATUS_EFFECT).ifPresent((statusEffects) -> {
				for (StatusEffectInstanceData data : this.effects) {
					if (testConditions(data.conditions, entity)) {
						statusEffects.getEntry(data.id).ifPresent(statusEffectReference -> entity.addStatusEffect(new StatusEffectInstance(statusEffectReference, data.duration, data.amplifier, data.ambient, data.showParticles, data.showIcon)));
					}
				}
			});
		}

		public static StatusEffectAction fromJsonArray(JsonArray array) {
			List<StatusEffectInstanceData> effects = new ArrayList<>();
			for (JsonElement element : array) {
				if (element instanceof JsonObject json) {
					StatusEffectInstanceData statusEffect = StatusEffectInstanceData.fromJson(json);
					if (statusEffect != null) effects.add(statusEffect);
				}
			}
			return new StatusEffectAction(effects);
		}

		public @NotNull String toString() {
			return "StatusEffectAction[effects=" + effects + "]";
		}

		public record StatusEffectInstanceData(Identifier id, int duration, int amplifier, boolean ambient, boolean showParticles, boolean showIcon, List<RelicCondition> conditions) {
			public JsonObject toJson() {
				JsonObject json = new JsonObject();
				json.addProperty("type", this.id.toString());
				JsonObject value = new JsonObject();
				value.addProperty("duration", this.duration);
				value.addProperty("amplifier", this.amplifier);
				value.addProperty("ambient", this.ambient);
				value.addProperty("show_particles", this.showParticles);
				value.addProperty("show_icon", this.showIcon);
				json.add("value", value);
				if (!this.conditions.isEmpty()) {
					JsonArray jsonConditions = new JsonArray();
					for (RelicCondition condition : this.conditions) jsonConditions.add(condition.toJson());
					json.add("conditions", jsonConditions);
				}
				return json;
			}

			public static StatusEffectInstanceData fromJson(JsonObject json) {
				JsonObject value = json.has("value") ? json.get("value").getAsJsonObject() : null;
				if (value == null) return null;
				List<RelicCondition> conditions = new ArrayList<>();
				if (json.has("conditions")) {
					for (JsonElement jsonElement : json.get("conditions").getAsJsonArray()) {
						if (jsonElement instanceof JsonObject jsonObject) conditions.add(RelicCondition.fromJson(jsonObject));
					}
				}
				return new StatusEffectInstanceData(Identifier.of(json.get("type").getAsString()), value.has("duration") ? value.get("duration").getAsInt() : 1, value.has("amplifier") ? value.get("amplifier").getAsInt() : 0, !value.has("ambient") || value.get("ambient").getAsBoolean(), !value.has("show_particles") || value.get("show_particles").getAsBoolean(), !value.has("show_icon") || value.get("show_icon").getAsBoolean(), conditions);
			}
		}
	}

	public interface RelicCondition {
		boolean test(LivingEntity entity);
		JsonObject toJson();

		Map<Identifier, Function<JsonObject, RelicCondition>> registry = new HashMap<>();

		static void register(Identifier type, Function<JsonElement, RelicCondition> parser) {
			registry.put(type, json -> {
				boolean inverted = false;
				JsonElement valueElement = null;

				if (json.has("inverted")) inverted = json.get("inverted").getAsBoolean();
				if (json.has("value")) valueElement = json.get("value");

				if (valueElement == null) valueElement = new JsonObject();

				RelicCondition condition = parser.apply(valueElement);
				return inverted ? new InvertedCondition(condition) : condition;
			});
		}

		static void register(Identifier type, Supplier<RelicCondition> factory) {
			registry.put(type, json -> {
				boolean inverted = json.has("inverted") && json.get("inverted").getAsBoolean();
				RelicCondition condition = factory.get();
				return inverted ? new InvertedCondition(condition) : condition;
			});
		}


		static RelicCondition fromJson(JsonObject json) {
			Identifier type = Identifier.of(json.get("type").getAsString());
			Function<JsonObject, RelicCondition> parser = registry.get(type);
			if (parser == null) throw new IllegalArgumentException("Unknown condition type: " + type);
			return parser.apply(json);
		}

		record InvertedCondition(RelicCondition condition) implements RelicCondition {
			public JsonObject toJson() {
				JsonObject json = condition.toJson();
				json.addProperty("inverted", true);
				return json;
			}

			public boolean test(LivingEntity entity) {
				return !condition.test(entity);
			}

			public @NotNull String toString() {
				return "InvertedCondition[condition=" + condition + "]";
			}
		}
	}

	public record AndCondition(List<RelicCondition> value) implements RelicCondition {
		public JsonObject toJson() {
			JsonObject json = new JsonObject();
			json.addProperty("type", ConditionTypes.and.toString());
			JsonArray array = new JsonArray();
			for (RelicCondition cond : this.value) array.add(cond.toJson());
			json.add("value", array);
			return json;
		}

		public boolean test(LivingEntity entity) {
			return value.stream().allMatch(condition -> condition.test(entity));
		}

		public static AndCondition fromJsonArray(JsonArray array) {
			List<RelicCondition> conditions = new ArrayList<>();
			for (JsonElement jsonElement : array) {
				if (jsonElement instanceof JsonObject jsonObject) conditions.add(RelicCondition.fromJson(jsonObject));
			}
			return new AndCondition(conditions);
		}

		public @NotNull String toString() {
			return "AndCondition[value=" + this.value + "]";
		}
	}

	public record OrCondition(List<RelicCondition> value) implements RelicCondition {
		public JsonObject toJson() {
			JsonObject json = new JsonObject();
			json.addProperty("type", ConditionTypes.or.toString());
			JsonArray array = new JsonArray();
			for (RelicCondition cond : this.value) array.add(cond.toJson());
			json.add("value", array);
			return json;
		}

		public boolean test(LivingEntity entity) {
			return this.value.stream().anyMatch(condition -> condition.test(entity));
		}

		public static OrCondition fromJsonArray(JsonArray array) {
			List<RelicCondition> conditions = new ArrayList<>();
			for (JsonElement jsonElement : array) {
				if (jsonElement instanceof JsonObject jsonObject) conditions.add(RelicCondition.fromJson(jsonObject));
			}
			return new OrCondition(conditions);
		}

		public @NotNull String toString() {
			return "OrCondition[value=" + this.value + "]";
		}
	}

	public record InBiomeCondition(List<Identifier> biomeIds, List<Identifier> biomeTags) implements RelicCondition {
		public JsonObject toJson() {
			JsonObject json = new JsonObject();
			json.addProperty("type", ConditionTypes.inBiome.toString());
			JsonArray array = new JsonArray();
			for (Identifier id : this.biomeIds) array.add(id.toString());
			for (Identifier tag : this.biomeTags) array.add("#" + tag.toString());
			json.add("value", array);
			return json;
		}

		public boolean test(LivingEntity entity) {
			RegistryEntry<Biome> biome = entity.getWorld().getBiome(entity.getBlockPos());
			for (Identifier id : this.biomeIds) if (biome.matchesId(id)) return true;
			for (Identifier tag : this.biomeTags) if (biome.isIn(TagKey.of(RegistryKeys.BIOME, tag))) return true;
			return false;
		}

		public static InBiomeCondition fromJson(JsonElement jsonElement) {
			List<Identifier> biomeIds = new ArrayList<>();
			List<Identifier> biomeTags = new ArrayList<>();
			if (jsonElement.isJsonArray()) {
				for (JsonElement element : jsonElement.getAsJsonArray()) parseBiome(element.getAsString(), biomeIds, biomeTags);
			} else parseBiome(jsonElement.getAsString(), biomeIds, biomeTags);
			return new InBiomeCondition(biomeIds, biomeTags);
		}

		public static void parseBiome(String id, List<Identifier> biomeIds, List<Identifier> biomeTags) {
			if (id.startsWith("#")) biomeTags.add(Identifier.of(id.substring(1)));
			else biomeIds.add(Identifier.of(id));
		}

		public @NotNull String toString() {
			return "InBiomeCondition[biomeIds=" + this.biomeIds + ", biomeTags=" + this.biomeTags + "]";
		}
	}

	public record TimeOfDayCondition(Long min, Long max) implements RelicCondition {
		public JsonObject toJson() {
			JsonObject json = new JsonObject();
			json.addProperty("type", ConditionTypes.timeOfDay.toString());
			JsonObject value = new JsonObject();
			value.addProperty("min", this.min != null ? this.min : 0);
			value.addProperty("max", this.max != null ? this.max : 24000);
			json.add("value", value);
			return json;
		}

		public boolean test(LivingEntity entity) {
			long time = entity.getWorld().getTimeOfDay() % 24000;
			if (min != null && max != null && min > max) return time >= min || time <= max;
			if (min != null && time < min) return false;
			return max == null || time <= max;
		}

		public static TimeOfDayCondition fromJson(JsonObject json) {
			return new TimeOfDayCondition(json.has("min") ? json.get("min").getAsLong() : null, json.has("max") ? json.get("max").getAsLong() : null);
		}

		public @NotNull String toString() {
			return "TimeOfDayCondition[min=" + this.min + ", max=" + this.max + "]";
		}
	}

	public record ArcaNocturnaCondition(OrCondition orCondition) implements RelicCondition {
		public JsonObject toJson() {
			return orCondition.toJson();
		}

		public boolean test(LivingEntity entity) {
			return orCondition.test(entity);
		}

		public static ArcaNocturnaCondition build() {
			return new ArcaNocturnaCondition(new OrCondition(List.of(new InBiomeCondition(List.of(), List.of(TagRegistry.WorldGen.Biome.somnium_reale.id())), new TimeOfDayCondition(12000L, 0L))));
		}

		public @NotNull String toString() {
			return "ArcaNocturnaCondition[orCondition=" + this.orCondition + "]";
		}
	}

	public static class ActionTypes {
		public static final Identifier statusEffect = Data.idOf("status_effect");
	}

	public static class ConditionTypes {
		public static final Identifier and = Data.idOf("and");
		public static final Identifier or = Data.idOf("or");
		public static final Identifier inBiome = Data.idOf("in_biome");
		public static final Identifier timeOfDay = Data.idOf("time_of_day");
		public static final Identifier arca_nocturna = Data.idOf("arca_nocturna");
	}

	public static boolean testConditions(List<RelicCondition> conditions, LivingEntity entity) {
		for (RelicCondition condition : conditions) {
			if (!condition.test(entity)) return false;
		}
		return true;
	}

	public static Relic fromJson(JsonObject jsonObject) {
		List<RelicCondition> conditions = new ArrayList<>();
		if (jsonObject.has("conditions")) {
			for (JsonElement jsonElement : jsonObject.getAsJsonArray("conditions")) {
				if (jsonElement instanceof JsonObject conditionObject) conditions.add(RelicCondition.fromJson(conditionObject));
			}
		}
		return new Relic(RelicAction.fromJson(jsonObject), conditions);
	}

	public static void bootstrap() {
		registerActions();
		registerConditions();
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new RelicLoader("relic"));
	}

	private static void registerActions() {
		RelicAction.register(ActionTypes.statusEffect, json -> StatusEffectAction.fromJsonArray(json.getAsJsonArray()));
	}

	private static void registerConditions() {
		RelicCondition.register(ConditionTypes.and, json -> AndCondition.fromJsonArray(json.getAsJsonArray()));
		RelicCondition.register(ConditionTypes.or, json -> OrCondition.fromJsonArray(json.getAsJsonArray()));
		RelicCondition.register(ConditionTypes.inBiome, InBiomeCondition::fromJson);
		RelicCondition.register(ConditionTypes.timeOfDay, json -> TimeOfDayCondition.fromJson(json.getAsJsonObject()));
		RelicCondition.register(ConditionTypes.arca_nocturna, ArcaNocturnaCondition::build);
	}

	public @NotNull String toString() {
		return "Relic[action=" + this.action + ", conditions=" + this.conditions + "]";
	}
}
