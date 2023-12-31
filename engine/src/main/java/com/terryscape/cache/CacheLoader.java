package com.terryscape.cache;

import com.terryscape.cache.item.ItemDefinition;
import com.terryscape.cache.npc.NpcDefinition;
import com.terryscape.world.WorldRegionCoordinate;

import java.util.Optional;
import java.util.Set;

public interface CacheLoader {

    ItemDefinition getItem(String id);

    Optional<ItemDefinition> getItemSafe(String id);

    NpcDefinition getNpc(String id);

    Set<WorldRegionCoordinate> getAllWorldRegions();

}
