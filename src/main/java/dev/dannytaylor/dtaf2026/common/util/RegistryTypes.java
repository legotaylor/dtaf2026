/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.util;

import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class RegistryTypes {
	public static class GenericRegistry<A, B> {
		protected final Map<A, B> registry;

		public GenericRegistry(Map<A, B> defaultRegistry) {
			registry = defaultRegistry;
		}

		public GenericRegistry() {
			this(new HashMap<>());
		}

		public Map<A, B> getRegistry() {
			return new HashMap<>(registry);
		}

		public boolean setRegistry(Map<A, B> updatedRegistry) {
			this.registry.clear();
			this.registry.putAll(updatedRegistry);
			return true;
		}

		public B get(A id) {
			return this.registry.get(id);
		}

		public B getOrDefault(A id, B fallback) {
			B value = get(id);
			return value != null ? value : fallback;
		}

		public A getOrDefaultId(A id, A fallback) {
			return registry.containsKey(id) ? id : fallback;
		}
	}

	public static class VariantRegistry<A extends Identifier, B> extends GenericRegistry<A, B> {
		private A defaultId;

		public VariantRegistry(A defaultId) {
			super();
			this.defaultId = defaultId;
		}

		public VariantRegistry(A defaultId, Map<A, B> defaultRegistry) {
			super(defaultRegistry);
			this.defaultId = defaultId;
		}

		public A getDefaultId() {
			return this.defaultId;
		}

		public void setDefaultId(A updatedDefaultId) {
			this.defaultId = updatedDefaultId;
		}

		public B getDefault() {
			return get(getDefaultId());
		}

		public B getOrDefault(A id) {
			return getOrDefault(id, getDefault());
		}

		public A getOrDefaultId(A id) {
			return getOrDefaultId(id, getDefaultId());
		}

		public boolean setRegistry(A updatedDefaultId, Map<A, B> updatedRegistry) {
			setDefaultId(updatedDefaultId);
			setRegistry(updatedRegistry);
			return true;
		}
	}
}
