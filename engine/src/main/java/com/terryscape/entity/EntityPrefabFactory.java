package com.terryscape.entity;

import com.terryscape.cache.npc.NpcDefinition;

public interface EntityPrefabFactory {

    Entity createNpcPrefab(NpcDefinition npcDefinition);

    Entity createPlayerPrefab();

}
