package com.terryscape.entity;

import com.terryscape.cache.npc.NpcDefinition;
import com.terryscape.game.combat.ParticleComponent;

public interface EntityPrefabFactory {

    Entity createNpcPrefab(NpcDefinition npcDefinition);

    Entity createPlayerPrefab();

    ParticleComponent createParticle();
}
