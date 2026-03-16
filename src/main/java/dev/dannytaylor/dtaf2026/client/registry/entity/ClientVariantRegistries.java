/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry.entity;

import dev.dannytaylor.dtaf2026.common.data.Data;
import dev.dannytaylor.dtaf2026.common.registry.entity.boar.BoarVariant;
import dev.dannytaylor.dtaf2026.common.registry.entity.boar.BoarVariants;
import dev.dannytaylor.dtaf2026.common.registry.entity.junglefowl.JunglefowlVariant;
import dev.dannytaylor.dtaf2026.common.registry.entity.junglefowl.JunglefowlVariants;
import dev.dannytaylor.dtaf2026.common.util.RegistryTypes;
import net.minecraft.util.Identifier;

import java.util.Map;

public class ClientVariantRegistries {
	public static final ClientVariantRegistries instance;

	static {
		instance = new ClientVariantRegistries();
	}

	public RegistryTypes.VariantRegistry<Identifier, JunglefowlVariant> junglefowlVariants;
	public RegistryTypes.VariantRegistry<Identifier, BoarVariant> boarVariants;

	public ClientVariantRegistries() {
		this.resetAll();
	}

	public static void bootstrap() {
	}

	public void resetAll() {
		this.junglefowlVariants = new RegistryTypes.VariantRegistry<>(Data.idOf("red"), JunglefowlVariants.getDefault());
		this.boarVariants = new RegistryTypes.VariantRegistry<>(Data.idOf("temperate"), BoarVariants.getDefault());
	}

	public void setAll(Map<Identifier, JunglefowlVariant> junglefowl, Map<Identifier, BoarVariant> boar) {
		this.junglefowlVariants.setRegistry(junglefowl);
		this.boarVariants.setRegistry(boar);
	}
}
