/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.item;

import dev.dannytaylor.dtaf2026.common.data.Data;
import dev.dannytaylor.dtaf2026.common.registry.item.component.ArcaNocturnaContentsComponent;
import dev.dannytaylor.dtaf2026.common.registry.item.component.RelicComponent;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.UnaryOperator;

public class ComponentTypeRegistry {
	public static final ComponentType<RelicComponent> relic;
	public static final ComponentType<ArcaNocturnaContentsComponent> arcaNocturnaContents;

	public static <T> ComponentType<T> register(Identifier id, UnaryOperator<ComponentType.Builder<T>> builder) {
		return Registry.register(Registries.DATA_COMPONENT_TYPE, id, builder.apply(ComponentType.builder()).build());
	}

	public static <T> ComponentType<T> register(String id, UnaryOperator<ComponentType.Builder<T>> builder) {
		return register(Data.idOf(id), builder);
	}

	public static void bootstrap() {
	}

	static {
		relic = register("relic", (builder) -> builder.codec(RelicComponent.codec).packetCodec(RelicComponent.packetCodec).cache());
		arcaNocturnaContents = register("arca_nocturna_contents", (builder) -> builder.codec(ArcaNocturnaContentsComponent.codec).packetCodec(ArcaNocturnaContentsComponent.packetCodec).cache());
	}
}
