package com.terryscape.cache.npc;

import java.util.Optional;

public interface NpcDefinition {

    String getId();

    String getName();

    String getDescription();

    boolean isInteractable();

    boolean isAttackable();

    NpcDefinitionNpcAppearanceType getAppearanceType();

    Optional<NpcDefinitionSimpleNpc> getSimpleNpc();

}
