package com.terryscape.game.loottable;

import com.terryscape.cache.npc.NpcDefinition;

import java.util.Set;

public interface LootTableProvider {

    Set<String> getNpcIds();

    LootTable getLootTable(NpcDefinition npcDefinition);
}
