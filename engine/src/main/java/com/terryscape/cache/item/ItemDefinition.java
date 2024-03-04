package com.terryscape.cache.item;

import java.util.Optional;

public interface ItemDefinition {

    String getId();

    String getName();

    String getDescription();

    boolean isStackable();

    Optional<EquipItemDefinition> getEquipDefinition();

    EquipItemDefinition getEquipDefinitionOrThrow();

    ItemStatsDefinition getItemStatsDefinition();
}
