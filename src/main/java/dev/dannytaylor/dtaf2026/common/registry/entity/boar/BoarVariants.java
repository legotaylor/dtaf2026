/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.entity.boar;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.serialization.JsonOps;
import dev.dannytaylor.dtaf2026.common.data.Data;
import dev.dannytaylor.dtaf2026.common.registry.JsonResourceLoader;
import dev.dannytaylor.dtaf2026.common.registry.RegistryTypes;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.entity.VariantSelectorProvider;
import net.minecraft.entity.spawn.SpawnCondition;
import net.minecraft.entity.spawn.SpawnConditionSelectors;
import net.minecraft.entity.spawn.SpawnContext;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.Util;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.profiler.Profiler;

import java.util.*;
import java.util.stream.Stream;

public class BoarVariants extends JsonResourceLoader implements IdentifiableResourceReloadListener {
	public static final RegistryTypes.VariantRegistry<Identifier, BoarVariant> variants;

	static {
		variants = new RegistryTypes.VariantRegistry<>(Data.idOf("temperate"));
	}

	public BoarVariants() {
		super(new Gson(), "boar_variant");
	}

	public static void bootstrap() {
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new BoarVariants());
	}

	public static Map<Identifier, BoarVariant> getDefault() {
		Pair<Identifier, BoarVariant> temperate = getDefaultVariant("temperate", BoarVariant.Model.temperate);
		return new HashMap<>(Map.of(temperate.getLeft(), temperate.getRight()));
	}

	public static Pair<Identifier, BoarVariant> getDefaultVariant(String id, BoarVariant.Model model) {
		return getDefaultVariant(Data.idOf(id), model);
	}

	public static Pair<Identifier, BoarVariant> getDefaultVariant(Identifier type, BoarVariant.Model model) {
		return new Pair<>(type, getDefault(model, type.withSuffixedPath("_boar"), SpawnConditionSelectors.createFallback(0)));
	}

	private static BoarVariant getDefault(BoarVariant.Model model, String textureName, SpawnConditionSelectors spawnConditions) {
		return getDefault(model, Data.idOf(textureName), spawnConditions);
	}

	private static BoarVariant getDefault(BoarVariant.Model model, Identifier textureName, SpawnConditionSelectors spawnConditions) {
		return new BoarVariant(new BoarVariant.ModelAndTextureAndTexture<>(model, textureName.withPrefixedPath("entity/boar/"), textureName.withPrefixedPath("entity/boar/").withSuffixedPath("_baby")), spawnConditions);
	}

	public static Optional<Identifier> select(Random random, SpawnContext context) {
		return Util.getRandomOrEmpty(test(context).toList(), random);
	}

	public static Stream<Identifier> test(SpawnContext context) {
		ArrayList<VariantSelectorProvider.UnwrappedSelector<SpawnContext, Identifier>> list = new ArrayList<>();
		variants.getRegistry().forEach((a, b) -> {
			for (VariantSelectorProvider.Selector<SpawnContext, SpawnCondition> selector : b.getSelectors())
				list.add(new VariantSelectorProvider.UnwrappedSelector<>(a, selector.priority(), DataFixUtils.orElseGet(selector.condition(), VariantSelectorProvider.SelectorCondition::alwaysTrue)));
		});
		list.sort(VariantSelectorProvider.UnwrappedSelector.PRIORITY_COMPARATOR);
		Iterator<VariantSelectorProvider.UnwrappedSelector<SpawnContext, Identifier>> iterator = list.iterator();
		int i = Integer.MIN_VALUE;
		while (iterator.hasNext()) {
			VariantSelectorProvider.UnwrappedSelector<SpawnContext, Identifier> unwrappedSelector = iterator.next();
			if (unwrappedSelector.priority() < i) {
				iterator.remove();
				continue;
			}
			if (unwrappedSelector.condition().test(context)) {
				i = unwrappedSelector.priority();
				continue;
			}
			iterator.remove();
		}
		return list.stream().map(VariantSelectorProvider.UnwrappedSelector::entry);
	}

	@Override
	protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
		if (!variants.getFrozen()) {
			Map<Identifier, BoarVariant> variants = getDefault();
			for (Map.Entry<Identifier, JsonElement> entry : prepared.entrySet()) {
				Identifier resourceId = entry.getKey();
				try {
					BoarVariant variant = BoarVariant.codec.parse(JsonOps.INSTANCE, entry.getValue()).result().orElseThrow(() -> new IllegalStateException("Failed to decode boar variant: " + resourceId));
					variants.put(resourceId, variant);
				} catch (Exception error) {
					Data.getLogger().error("Failed to parse boar variant {}: ", resourceId, error);
				}
			}
			setRegistry(variants);
		}
	}

	private void setRegistry(Map<Identifier, BoarVariant> variants) {
		if (BoarVariants.variants.setRegistry(variants)) BoarVariants.variants.freeze();
	}

	@Override
	public Identifier getFabricId() {
		return Data.idOf(resourceLocation);
	}
}
