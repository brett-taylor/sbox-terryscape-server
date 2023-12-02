package com.terryscape.cache;

import com.terryscape.cache.item.ItemDefinition;
import com.terryscape.cache.npc.NpcDefinition;

public interface CacheLoader {

    ItemDefinition getItem(String id);

    NpcDefinition getNpc(String id);

}
