package com.terryscape.cache;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.item.ItemCacheLoader;
import com.terryscape.cache.item.ItemDefinition;
import com.terryscape.cache.item.ItemDefinitionImpl;
import com.terryscape.cache.npc.NpcCacheLoader;
import com.terryscape.cache.npc.NpcDefinition;
import com.terryscape.cache.npc.NpcDefinitionImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Singleton
public class CacheLoaderImpl implements CacheLoader {

    private static final Logger LOGGER = LogManager.getLogger(CacheLoaderImpl.class);

    private final ItemCacheLoader itemCacheLoader;

    private final NpcCacheLoader npcCacheLoader;

    private final Map<String, ItemDefinitionImpl> items = new HashMap<>();

    private final Map<String, NpcDefinitionImpl> npcs = new HashMap<>();

    @Inject
    public CacheLoaderImpl(ItemCacheLoader itemCacheLoader, NpcCacheLoader npcCacheLoader) {
        this.itemCacheLoader = itemCacheLoader;
        this.npcCacheLoader = npcCacheLoader;
    }

    public void loadCache() {
        var startTime = System.currentTimeMillis();

        try {
            loadItems();
            loadNpcs();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load cache", e);
        }

        LOGGER.info("Loading cache in {} milliseconds.", System.currentTimeMillis() - startTime);
    }

    @Override
    public ItemDefinition getItem(String id) {
        if (!items.containsKey(id)) {
            throw new RuntimeException("No item found with id %s".formatted(id));
        }

        return items.get(id);
    }

    @Override
    public Optional<ItemDefinition> getItemSafe(String id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public NpcDefinition getNpc(String id) {
        if (!npcs.containsKey(id)) {
            throw new RuntimeException("No npc found with id %s".formatted(id));
        }

        return npcs.get(id);
    }

    private void loadItems() throws IOException {
        items.putAll(itemCacheLoader.readItemsFromCache());
        LOGGER.info("Loaded {} Items.", items.size());
    }

    private void loadNpcs() throws IOException {
        npcs.putAll(npcCacheLoader.readNpcsFromCache());
        LOGGER.info("Loaded {} Npcs.", npcs.size());
    }

}
