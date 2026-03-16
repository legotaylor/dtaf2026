/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.entity;

import dev.dannytaylor.dtaf2026.common.data.Data;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricTrackedDataRegistry;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.util.Identifier;

public class TrackedDataRegistry {
	public static final TrackedDataHandler<Identifier> variant;

	static {
		variant = TrackedDataHandler.create(Identifier.PACKET_CODEC);
		FabricTrackedDataRegistry.register(Data.idOf("variant"), variant);
	}

	public static void bootstrap() {
	}
}
