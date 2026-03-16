/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.common.registry;

import dev.dannytaylor.dtaf2026.common.registry.advancement.CriteriaRegistry;
import dev.dannytaylor.dtaf2026.common.registry.advancement.LootConditionTypeRegistry;
import dev.dannytaylor.dtaf2026.common.registry.advancement.LootContextParameterRegistry;
import dev.dannytaylor.dtaf2026.common.registry.advancement.LootContextTypeRegistry;

public class AdvancementRegistry {
	public static void bootstrap() {
		LootContextTypeRegistry.bootstrap();
		LootContextParameterRegistry.bootstrap();
		LootConditionTypeRegistry.bootstrap();
		CriteriaRegistry.bootstrap();
	}
}
