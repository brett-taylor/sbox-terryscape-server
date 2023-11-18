package com.terryscape.cache;

public interface CacheLoader {

    ItemDefinition getItem(String id);

    NpcDefinition getNpc(String id);

}
