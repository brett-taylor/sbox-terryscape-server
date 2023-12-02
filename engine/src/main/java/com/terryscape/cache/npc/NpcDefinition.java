package com.terryscape.cache.npc;

import java.util.Optional;

public interface NpcDefinition {

    String getId();

    String getName();

    String getDescription();

    Optional<NpcDefinitionSimpleNpc> getSimpleNpc();

}
