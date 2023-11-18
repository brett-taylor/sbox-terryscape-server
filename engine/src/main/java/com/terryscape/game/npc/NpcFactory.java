package com.terryscape.game.npc;

import com.terryscape.cache.NpcDefinition;
import com.terryscape.entity.Entity;

public interface NpcFactory {

    Entity createUnregisteredNpc(NpcDefinition npcDefinition);
}
