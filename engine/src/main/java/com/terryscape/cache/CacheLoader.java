package com.terryscape.cache;

import com.terryscape.cache.item.ItemDefinition;
import com.terryscape.cache.npc.NpcDefinition;

import java.util.Optional;

public interface CacheLoader {

    ItemDefinition getItem(String id);

    Optional<ItemDefinition> getItemSafe(String id);

    NpcDefinition getNpc(String id);

}
