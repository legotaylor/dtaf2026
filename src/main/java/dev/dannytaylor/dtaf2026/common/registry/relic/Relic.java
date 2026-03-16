/*
    Somnium Reale
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
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
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
	public static final RelicLoader data;

	private boolean active;
	public RelicAction action;
	public List<RelicCondition> conditions;

	public Relic(RelicAction action, List<RelicCondition> conditions) {
		this.action = action;
		this.conditions = conditions;
	}

	public void tick(LivingEntity livingEntity, ItemStack bundle) {
		if (testConditions(conditions, livingEntity, bundle)) {
			this.action.apply(livingEntity, bundle, this.active);
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
		void apply(LivingEntity entity, ItemStack bundle, boolean active);

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

		public void apply(LivingEntity entity, ItemStack bundle, boolean active) {
			entity.getWorld().getRegistryManager().getOptional(RegistryKeys.STATUS_EFFECT).ifPresent((statusEffects) -> {
				for (StatusEffectInstanceData data : this.effects) {
					if (testConditions(data.conditions, entity, bundle)) {
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
				return new StatusEffectInstanceData(Identifier.of(json.get("type").getAsString()), value.has("duration") ? value.get("duration").getAsInt() : 30, value.has("amplifier") ? value.get("amplifier").getAsInt() : 0, !value.has("ambient") || value.get("ambient").getAsBoolean(), !value.has("show_particles") || value.get("show_particles").getAsBoolean(), !value.has("show_icon") || value.get("show_icon").getAsBoolean(), conditions);
			}
		}
	}

	public interface RelicCondition {
		boolean test(LivingEntity entity, ItemStack bundle);
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

			public boolean test(LivingEntity entity, ItemStack bundle) {
				return !condition.test(entity, bundle);
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

		public boolean test(LivingEntity entity, ItemStack bundle) {
			return value.stream().allMatch(condition -> condition.test(entity, bundle));
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

		public boolean test(LivingEntity entity, ItemStack bundle) {
			return this.value.stream().anyMatch(condition -> condition.test(entity, bundle));
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

		public boolean test(LivingEntity entity, ItemStack bundle) {
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

	public record TimeOfDayCondition(Long min, Long max, Boolean raw) implements RelicCondition {
		public JsonObject toJson() {
			JsonObject json = new JsonObject();
			json.addProperty("type", ConditionTypes.timeOfDay.toString());
			JsonObject value = new JsonObject();
			value.addProperty("min", this.min != null ? this.min : 0);
			value.addProperty("max", this.max != null ? this.max : 24000);
			json.add("value", value);
			return json;
		}

		public boolean test(LivingEntity entity, ItemStack bundle) {
			long time = entity.getWorld().getTimeOfDay() % 24000;
			if (!raw) {
				if (entity.getWorld().getDimension().hasFixedTime()) {
					OptionalLong fixedTime = entity.getWorld().getDimension().fixedTime();
					if (fixedTime.isPresent()) time = fixedTime.getAsLong();
				}
			}
			if (min != null && max != null && min > max) return time >= min || time <= max;
			if (min != null && time < min) return false;
			return max == null || time <= max;
		}

		public static TimeOfDayCondition fromJson(JsonObject json) {
			return new TimeOfDayCondition(json.has("min") ? json.get("min").getAsLong() : null, json.has("max") ? json.get("max").getAsLong() : null, json.has("raw") && json.get("raw").getAsBoolean());
		}

		public @NotNull String toString() {
			return "TimeOfDayCondition[min=" + this.min + ", max=" + this.max + ", raw=" + this.raw + "]";
		}
	}

	public record NightCondition(TimeOfDayCondition timeOfDayCondition) implements RelicCondition {
		public JsonObject toJson() {
			return timeOfDayCondition.toJson();
		}

		public boolean test(LivingEntity entity, ItemStack bundle) {
			return timeOfDayCondition.test(entity, bundle);
		}

		public static NightCondition build() {
			return new NightCondition(new TimeOfDayCondition(12000L, 0L, false));
		}

		public @NotNull String toString() {
			return "NightCondition[timeOfDayCondition=" + this.timeOfDayCondition + "]";
		}
	}

	public record DayCondition(TimeOfDayCondition timeOfDayCondition) implements RelicCondition {
		public JsonObject toJson() {
			return timeOfDayCondition.toJson();
		}

		public boolean test(LivingEntity entity, ItemStack bundle) {
			return timeOfDayCondition.test(entity, bundle);
		}

		public static DayCondition build() {
			return new DayCondition(new TimeOfDayCondition(0L, 12000L, false));
		}

		public @NotNull String toString() {
			return "DayCondition[timeOfDayCondition=" + this.timeOfDayCondition + "]";
		}
	}

	public record InBundleCondition(List<Identifier> bundleIds, List<Identifier> bundleTags) implements RelicCondition {
		public JsonObject toJson() {
			JsonObject json = new JsonObject();
			json.addProperty("type", ConditionTypes.inBiome.toString());
			JsonArray array = new JsonArray();
			for (Identifier id : this.bundleIds) array.add(id.toString());
			for (Identifier tag : this.bundleTags) array.add("#" + tag.toString());
			json.add("value", array);
			return json;
		}

		public boolean test(LivingEntity entity, ItemStack bundle) {
			for (Identifier id : this.bundleIds) if (bundle.isOf(Registries.ITEM.get(id))) return true;
			for (Identifier tag : this.bundleTags) if (bundle.isIn(TagKey.of(RegistryKeys.ITEM, tag))) return true;
			return false;
		}

		public static InBundleCondition fromJson(JsonElement jsonElement) {
			List<Identifier> bundleIds = new ArrayList<>();
			List<Identifier> bundleTags = new ArrayList<>();
			if (jsonElement.isJsonArray()) {
				for (JsonElement element : jsonElement.getAsJsonArray()) parseBiome(element.getAsString(), bundleIds, bundleTags);
			} else parseBiome(jsonElement.getAsString(), bundleIds, bundleTags);
			return new InBundleCondition(bundleIds, bundleTags);
		}

		public static void parseBiome(String id, List<Identifier> bundleIds, List<Identifier> bundleTags) {
			if (id.startsWith("#")) bundleTags.add(Identifier.of(id.substring(1)));
			else bundleIds.add(Identifier.of(id));
		}

		public @NotNull String toString() {
			return "InBundleCondition[bundleIds=" + this.bundleIds + ", bundleTags=" + this.bundleTags + "]";
		}
	}

	public record InNightRelicBundleCondition(InBundleCondition inBundleCondition) implements RelicCondition {
		public JsonObject toJson() {
			return inBundleCondition.toJson();
		}

		public boolean test(LivingEntity entity, ItemStack bundle) {
			return inBundleCondition.test(entity, bundle);
		}

		public static InNightRelicBundleCondition build() {
			return new InNightRelicBundleCondition(new InBundleCondition(List.of(TagRegistry.Item.nightRelicBundle.id()), List.of()));
		}

		public @NotNull String toString() {
			return "InNightRelicBundleCondition[inBundleCondition=" + this.inBundleCondition + "]";
		}
	}

	public record InDayRelicBundleCondition(InBundleCondition inBundleCondition) implements RelicCondition {
		public JsonObject toJson() {
			return inBundleCondition.toJson();
		}

		public boolean test(LivingEntity entity, ItemStack bundle) {
			return inBundleCondition.test(entity, bundle);
		}

		public static InDayRelicBundleCondition build() {
			return new InDayRelicBundleCondition(new InBundleCondition(List.of(TagRegistry.Item.dayRelicBundle.id()), List.of()));
		}

		public @NotNull String toString() {
			return "InDayRelicBundleCondition[inBundleCondition=" + this.inBundleCondition + "]";
		}
	}

	public record InRelicBundleCondition(InBundleCondition inBundleCondition) implements RelicCondition {
		public JsonObject toJson() {
			return inBundleCondition.toJson();
		}

		public boolean test(LivingEntity entity, ItemStack bundle) {
			return inBundleCondition.test(entity, bundle);
		}

		public static InRelicBundleCondition build() {
			return new InRelicBundleCondition(new InBundleCondition(List.of(TagRegistry.Item.relicBundle.id()), List.of()));
		}

		public @NotNull String toString() {
			return "InRelicBundleCondition[inBundleCondition=" + this.inBundleCondition + "]";
		}
	}

	public record InActiveRelicBundleCondition(OrCondition orCondition) implements RelicCondition {
		public JsonObject toJson() {
			return orCondition.toJson();
		}

		public boolean test(LivingEntity entity, ItemStack bundle) {
			return orCondition.test(entity, bundle);
		}

		public static InActiveRelicBundleCondition build() {
			return new InActiveRelicBundleCondition(new OrCondition(List.of(new AndCondition(List.of(NightCondition.build(), InNightRelicBundleCondition.build())), new AndCondition(List.of(DayCondition.build(), InDayRelicBundleCondition.build())), InRelicBundleCondition.build())));
		}

		public @NotNull String toString() {
			return "InActiveRelicBundleCondition[orCondition=" + this.orCondition + "]";
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
		public static final Identifier night = Data.idOf("night");
		public static final Identifier day = Data.idOf("day");
		public static final Identifier inBundle = Data.idOf("in_bundle");
		public static final Identifier inNightRelicBundle = Data.idOf("in_night_relic_bundle");
		public static final Identifier inDayRelicBundle = Data.idOf("in_day_relic_bundle");
		public static final Identifier inRelicBundle = Data.idOf("in_relic_bundle");
		public static final Identifier inActiveRelicBundle = Data.idOf("in_active_relic_bundle");
	}

	public static boolean testConditions(List<RelicCondition> conditions, LivingEntity entity, ItemStack bundle) {
		for (RelicCondition condition : conditions) {
			if (!condition.test(entity, bundle)) return false;
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
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(data);
	}

	private static void registerActions() {
		RelicAction.register(ActionTypes.statusEffect, json -> StatusEffectAction.fromJsonArray(json.getAsJsonArray()));
	}

	private static void registerConditions() {
		RelicCondition.register(ConditionTypes.and, json -> AndCondition.fromJsonArray(json.getAsJsonArray()));
		RelicCondition.register(ConditionTypes.or, json -> OrCondition.fromJsonArray(json.getAsJsonArray()));
		RelicCondition.register(ConditionTypes.inBiome, InBiomeCondition::fromJson);
		RelicCondition.register(ConditionTypes.timeOfDay, json -> TimeOfDayCondition.fromJson(json.getAsJsonObject()));
		RelicCondition.register(ConditionTypes.night, NightCondition::build);
		RelicCondition.register(ConditionTypes.day, DayCondition::build);
		RelicCondition.register(ConditionTypes.inBundle, InBundleCondition::fromJson);
		RelicCondition.register(ConditionTypes.inNightRelicBundle, InNightRelicBundleCondition::build);
		RelicCondition.register(ConditionTypes.inDayRelicBundle, InDayRelicBundleCondition::build);
		RelicCondition.register(ConditionTypes.inRelicBundle, InRelicBundleCondition::build);
		RelicCondition.register(ConditionTypes.inActiveRelicBundle, InActiveRelicBundleCondition::build);
	}

	public @NotNull String toString() {
		return "Relic[action=" + this.action + ", conditions=" + this.conditions + "]";
	}

	static {
		data = new RelicLoader("relic");
	}
}
