/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.entity.junglefowl;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.serialization.JsonOps;
import dev.dannytaylor.dtaf2026.common.data.Data;
import dev.dannytaylor.dtaf2026.common.util.JsonResourceLoader;
import dev.dannytaylor.dtaf2026.common.util.RegistryTypes;
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

public class JunglefowlVariants extends JsonResourceLoader implements IdentifiableResourceReloadListener {
	public static final RegistryTypes.VariantRegistry<Identifier, JunglefowlVariant> variants;

	static {
		variants = new RegistryTypes.VariantRegistry<>(Data.idOf("red"));
	}

	public JunglefowlVariants() {
		super(new Gson(), "junglefowl_variant");
	}

	public static void bootstrap() {
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new JunglefowlVariants());
	}

	public static Map<Identifier, JunglefowlVariant> getDefault() {
		Pair<Identifier, JunglefowlVariant> red = getDefaultVariant("red", JunglefowlVariant.Model.small);
		Pair<Identifier, JunglefowlVariant> largeRed = getDefaultVariant("red", JunglefowlVariant.Model.big);
		Pair<Identifier, JunglefowlVariant> gray = getDefaultVariant("gray", JunglefowlVariant.Model.small);
		Pair<Identifier, JunglefowlVariant> largeGray = getDefaultVariant("gray", JunglefowlVariant.Model.big);
		return new HashMap<>(Map.of(red.getLeft(), red.getRight(), largeRed.getLeft(), largeGray.getRight(), gray.getLeft(), gray.getRight(), largeGray.getLeft(), largeGray.getRight()));
	}

	public static Pair<Identifier, JunglefowlVariant> getDefaultVariant(String id, JunglefowlVariant.Model model) {
		return getDefaultVariant(Data.idOf(id), model);
	}

	public static Pair<Identifier, JunglefowlVariant> getDefaultVariant(Identifier id, JunglefowlVariant.Model model) {
		Identifier type = id.withPrefixedPath(model.equals(JunglefowlVariant.Model.big) ? "large_" : "");
		return new Pair<>(type, getDefault(model, type.withSuffixedPath("_junglefowl"), SpawnConditionSelectors.createFallback(0)));
	}

	private static JunglefowlVariant getDefault(JunglefowlVariant.Model model, String textureName, SpawnConditionSelectors spawnConditions) {
		return getDefault(model, Data.idOf(textureName), spawnConditions);
	}

	private static JunglefowlVariant getDefault(JunglefowlVariant.Model model, Identifier textureName, SpawnConditionSelectors spawnConditions) {
		return new JunglefowlVariant(new JunglefowlVariant.ModelAndTextureAndTexture<>(model, textureName.withPrefixedPath("entity/junglefowl/"), textureName.withPrefixedPath("entity/junglefowl/").withSuffixedPath("_baby")), spawnConditions);
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
			Map<Identifier, JunglefowlVariant> variants = getDefault();
			for (Map.Entry<Identifier, JsonElement> entry : prepared.entrySet()) {
				Identifier resourceId = entry.getKey();
				try {
					JunglefowlVariant variant = JunglefowlVariant.codec.parse(JsonOps.INSTANCE, entry.getValue()).result().orElseThrow(() -> new IllegalStateException("Failed to decode junglefowl variant: " + resourceId));
					variants.put(resourceId, variant);
				} catch (Exception error) {
					Data.getLogger().error("Failed to parse junglefowl variant {}: ", resourceId, error);
				}
			}
			setRegistry(variants);
		}
	}

	private void setRegistry(Map<Identifier, JunglefowlVariant> variants) {
		if (JunglefowlVariants.variants.setRegistry(variants)) JunglefowlVariants.variants.freeze();
	}

	@Override
	public Identifier getFabricId() {
		return Data.idOf(resourceLocation);
	}
}
