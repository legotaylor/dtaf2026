/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import dev.dannytaylor.dtaf2026.common.data.Data;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SinglePreparationResourceReloader;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public abstract class JsonResourceLoader extends SinglePreparationResourceReloader<Map<Identifier, JsonElement>> {
	public final String resourceLocation;
	private final Gson gson;

	public JsonResourceLoader(Gson gson, String resourceLocation) {
		this.gson = gson;
		this.resourceLocation = resourceLocation;
	}

	public static void load(ResourceManager manager, String dataType, Gson gson, Map<Identifier, JsonElement> results) {
		ResourceFinder resourceFinder = ResourceFinder.json(dataType);
		for (Map.Entry<Identifier, Resource> identifierResourceEntry : resourceFinder.findResources(manager).entrySet()) {
			Identifier resourceEntryKey = identifierResourceEntry.getKey();
			Identifier resourceId = resourceFinder.toResourceId(resourceEntryKey);
			try {
				Reader reader = identifierResourceEntry.getValue().getReader();
				try {
					JsonElement jsonElement = results.put(resourceId, JsonHelper.deserialize(gson, reader, JsonElement.class));
					if (jsonElement != null)
						throw new IllegalStateException("Duplicate data file ignored with ID " + resourceId);
				} catch (Throwable throwable) {
					if (reader != null) {
						try {
							reader.close();
						} catch (Throwable var12) {
							throwable.addSuppressed(var12);
						}
					}
					throw throwable;
				}
				reader.close();
			} catch (Exception error) {
				Data.getLogger().error("Couldn't parse data file {} from {}: {}", resourceId, resourceEntryKey, error);
			}
		}
	}

	protected Map<Identifier, JsonElement> prepare(ResourceManager resourceManager, Profiler profiler) {
		Map<Identifier, JsonElement> map = new HashMap<>();
		load(resourceManager, this.resourceLocation, this.gson, map);
		return map;
	}
}
