/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.registry.item;

import dev.dannytaylor.dtaf2026.client.registry.item.model.ArcaNocturnaItemModel;
import dev.dannytaylor.dtaf2026.common.data.Data;
import net.minecraft.client.render.item.model.ItemModelTypes;

public class ItemModelRegistry {
	public static void bootstrap() {
		ItemModelTypes.ID_MAPPER.put(Data.idOf("relic_bundle/item"), ArcaNocturnaItemModel.Unbaked.CODEC);
	}
}
