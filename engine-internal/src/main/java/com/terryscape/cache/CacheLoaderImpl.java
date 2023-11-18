package com.terryscape.cache;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Singleton
public class CacheLoaderImpl implements CacheLoader {

    private static final Logger LOGGER = LogManager.getLogger(CacheLoaderImpl.class);

    private final Gson gson;

    private final Map<String, ItemDefinitionImpl> items = new HashMap<>();

    private final Map<String, NpcDefinitionImpl> npcs = new HashMap<>();

    @Inject
    public CacheLoaderImpl(Gson gson) {
        this.gson = gson;
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
    public NpcDefinition getNpc(String id) {
        if (!npcs.containsKey(id)) {
            throw new RuntimeException("No npc found with id %s".formatted(id));
        }

        return npcs.get(id);
    }

    private void loadItems() throws IOException {
        try (var jsonReader = new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream(Config.ITEM_CACHE_LOCATION)))) {
            var typeToDeserialize = new TypeToken<ArrayList<ItemDefinitionImpl>>() {
            }.getType();

            ArrayList<ItemDefinitionImpl> itemList = gson.fromJson(jsonReader, typeToDeserialize);
            itemList.forEach(itemDefinition -> items.put(itemDefinition.getId(), itemDefinition));
        }

        LOGGER.info("Loaded {} Items.", items.size());
    }

    private void loadNpcs() throws IOException {
        try (var jsonReader = new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream(Config.NPC_CACHE_LOCATION)))) {
            var typeToDeserialize = new TypeToken<ArrayList<NpcDefinitionImpl>>() {
            }.getType();

            ArrayList<NpcDefinitionImpl> npcList = gson.fromJson(jsonReader, typeToDeserialize);
            npcList.forEach(npcDefinition -> npcs.put(npcDefinition.getId(), npcDefinition));
        }

        LOGGER.info("Loaded {} Npcs.", npcs.size());
    }

}
