/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry.block;

import dev.dannytaylor.dtaf2026.common.data.Data;
import net.minecraft.block.BlockSetType;
import net.minecraft.block.WoodType;

public class WoodTypeRegistry {
	public static WoodType register(String path, BlockSetType type) {
		return WoodType.register(new WoodType(Data.idOf(path).toUnderscoreSeparatedString(), type));
	}
}
