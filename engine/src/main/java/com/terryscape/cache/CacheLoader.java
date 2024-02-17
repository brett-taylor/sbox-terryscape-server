package com.terryscape.cache;

import com.terryscape.cache.item.ItemDefinition;
import com.terryscape.cache.npc.NpcDefinition;
import com.terryscape.cache.object.ObjectDefinition;
import com.terryscape.cache.world.WorldRegionDefinition;
import com.terryscape.world.coordinate.WorldRegionCoordinate;

import java.util.Optional;

public interface CacheLoader {

    ItemDefinition getItem(String id);

    Optional<ItemDefinition> getItemSafe(String id);

    NpcDefinition getNpc(String id);

    ObjectDefinition getObjectDefinition(String id);

    WorldRegionDefinition getWorldRegion(WorldRegionCoordinate worldRegionCoordinate);
}
