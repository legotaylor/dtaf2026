/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.relic;

import com.google.gson.JsonParser;
import dev.dannytaylor.dtaf2026.common.data.Data;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RelicLoader implements SimpleSynchronousResourceReloadListener {
	private static final Map<Identifier, Relic> registry = new HashMap<>();
	private final String path;

	public RelicLoader(String path) {
		this.path = path;
	}

	public static void reset() {
		registry.clear();
	}

	public static Optional<Relic> get(Identifier id) {
		Relic relic = registry.get(id);
		return relic != null ? Optional.of(relic) : Optional.empty();
	}

	public static Map<Identifier, Relic> getAll() {
		return registry;
	}

	public void reload(ResourceManager manager) {
		reset();
		for (Map.Entry<Identifier, Resource> resource : manager.findResources(this.path, identifier -> identifier.getPath().endsWith(".json")).entrySet()) {
			Identifier id = resource.getKey();
			try (InputStream stream = resource.getValue().getInputStream()) {
				registry.put(id.withPath(id.getPath().substring(id.getPath().lastIndexOf("/") + 1, id.getPath().lastIndexOf(".json"))), Relic.fromJson(JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonObject()));
			} catch (Exception error) {
				Data.getLogger().error("Failed to load relic with id '{}': {}", id, error.getLocalizedMessage());
			}
		}
	}

	public Identifier getFabricId() {
		return Data.idOf(this.path);
	}
}
