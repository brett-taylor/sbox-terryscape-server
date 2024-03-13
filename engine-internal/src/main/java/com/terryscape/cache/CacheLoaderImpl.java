package com.terryscape.cache;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.item.*;
import com.terryscape.cache.npc.*;
import com.terryscape.cache.object.ObjectCacheLoader;
import com.terryscape.cache.object.ObjectDefinition;
import com.terryscape.cache.object.ObjectDefinitionImpl;
import com.terryscape.cache.projectile.ProjectileCacheLoader;
import com.terryscape.cache.projectile.ProjectileDefinition;
import com.terryscape.cache.projectile.ProjectileDefinitionImpl;
import com.terryscape.cache.sound.SoundCacheLoader;
import com.terryscape.cache.sound.SoundDefinition;
import com.terryscape.cache.sound.SoundDefinitionImpl;
import com.terryscape.cache.world.WorldRegionCacheLoader;
import com.terryscape.cache.world.WorldRegionDefinition;
import com.terryscape.cache.world.WorldRegionDefinitionImpl;
import com.terryscape.world.coordinate.WorldRegionCoordinate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Singleton
public class CacheLoaderImpl implements CacheLoader {

    private static final Logger LOGGER = LogManager.getLogger(CacheLoaderImpl.class);

    private final ItemCacheLoader itemCacheLoader;

    private final ItemStatsCacheLoader itemStatsCacheLoader;

    private final NpcCacheLoader npcCacheLoader;

    private final NpcStatsCacheLoader npcStatsCacheLoader;

    private final ObjectCacheLoader objectCacheLoader;

    private final WorldRegionCacheLoader worldRegionCacheLoader;

    private final SoundCacheLoader soundCacheLoader;

    private final ProjectileCacheLoader projectileCacheLoader;

    private final Map<String, ItemDefinitionImpl> items = new HashMap<>();

    private final Map<String, ItemStatsDefinition> itemStats = new HashMap<>();

    private final Map<String, NpcDefinitionImpl> npcs = new HashMap<>();

    private final Map<String, NpcStatsDefinition> npcStats = new HashMap<>();

    private final Map<String, ObjectDefinitionImpl> objects = new HashMap<>();

    private final Map<WorldRegionCoordinate, WorldRegionDefinitionImpl> worldRegions = new HashMap<>();

    private final Map<String, SoundDefinitionImpl> sounds = new HashMap<>();

    private final Map<String, ProjectileDefinitionImpl> projectiles = new HashMap<>();

    @Inject
    public CacheLoaderImpl(ItemCacheLoader itemCacheLoader,
                           ItemStatsCacheLoader itemStatsCacheLoader,
                           NpcCacheLoader npcCacheLoader,
                           NpcStatsCacheLoader npcStatsCacheLoader,
                           ObjectCacheLoader objectCacheLoader,
                           WorldRegionCacheLoader worldRegionCacheLoader,
                           SoundCacheLoader soundCacheLoader,
                           ProjectileCacheLoader projectileCacheLoader) {

        this.itemCacheLoader = itemCacheLoader;
        this.itemStatsCacheLoader = itemStatsCacheLoader;
        this.npcCacheLoader = npcCacheLoader;
        this.npcStatsCacheLoader = npcStatsCacheLoader;
        this.objectCacheLoader = objectCacheLoader;
        this.worldRegionCacheLoader = worldRegionCacheLoader;
        this.soundCacheLoader = soundCacheLoader;
        this.projectileCacheLoader = projectileCacheLoader;
    }

    public void loadCache() {
        var startTime = System.currentTimeMillis();

        try {
            loadItems();
            loadNpcs();
            loadObjects();
            loadWorldRegions();
            loadSounds();
            loadProjectiles();
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("Failed to load cache", e);
        }

        LOGGER.info("Loading cache in {} milliseconds.", System.currentTimeMillis() - startTime);
    }

    @Override
    public ItemDefinition getItemDefinition(String id) {
        if (!items.containsKey(id)) {
            throw new RuntimeException("No item found with id %s".formatted(id));
        }

        return items.get(id);
    }

    @Override
    public Optional<ItemDefinition> getItemDefinitionSafe(String id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public NpcDefinition getNpcDefinition(String id) {
        if (!npcs.containsKey(id)) {
            throw new RuntimeException("No npc found with id %s".formatted(id));
        }

        return npcs.get(id);
    }

    @Override
    public Optional<NpcDefinition> getNpcDefinitionSafe(String id) {
        return Optional.ofNullable(npcs.get(id));
    }

    @Override
    public ObjectDefinition getObjectDefinition(String id) {
        if (!objects.containsKey(id)) {
            throw new RuntimeException("No object found with id %s".formatted(id));
        }

        return objects.get(id);
    }

    @Override
    public WorldRegionDefinition getWorldRegionDefinition(WorldRegionCoordinate worldRegionCoordinate) {
        if (!worldRegions.containsKey(worldRegionCoordinate)) {
            throw new RuntimeException("No WorldRegion found with at %s".formatted(worldRegionCoordinate));
        }

        return worldRegions.get(worldRegionCoordinate);
    }

    @Override
    public SoundDefinition getSoundDefinition(String id) {
        if (!sounds.containsKey(id)) {
            throw new RuntimeException("No sound found with at %s".formatted(id));
        }

        return sounds.get(id);
    }

    @Override
    public Optional<SoundDefinition> getSoundDefinitionSafe(String id) {
        return Optional.ofNullable(sounds.get(id));
    }

    @Override
    public ProjectileDefinition getProjectileDefinition(String id) {
        if (!projectiles.containsKey(id)) {
            throw new RuntimeException("No projectile found with at %s".formatted(id));
        }

        return projectiles.get(id);
    }

    @Override
    public Optional<ProjectileDefinition> getProjectileDefinitionSafe(String id) {
        return Optional.ofNullable(projectiles.get(id));
    }

    private void loadItems() throws IOException {
        itemStats.putAll(itemStatsCacheLoader.readItemStatsFromCache());
        LOGGER.info("Loaded {} Item Stats.", itemStats.size());

        items.putAll(itemCacheLoader.readItemsFromCache(itemStats));
        LOGGER.info("Loaded {} Items.", items.size());
    }

    private void loadNpcs() throws IOException {
        npcStats.putAll(npcStatsCacheLoader.readNpcStatsFromCache());
        LOGGER.info("Loaded {} Npc Stats.", npcStats.size());

        npcs.putAll(npcCacheLoader.readNpcsFromCache(npcStats));
        LOGGER.info("Loaded {} Npcs.", npcs.size());
    }

    private void loadObjects() throws IOException {
        objects.putAll(objectCacheLoader.readObjectsFromCache());
        LOGGER.info("Loaded {} Objects.", objects.size());
    }

    private void loadWorldRegions() throws IOException, URISyntaxException {
        worldRegions.putAll(worldRegionCacheLoader.readWorldRegionsFromCache());
        LOGGER.info("Loaded {} World Regions.", worldRegions.size());
    }

    private void loadSounds() throws IOException {
        sounds.putAll(soundCacheLoader.readSoundsFromCache());
        LOGGER.info("Loaded {} Sounds.", sounds.size());
    }

    private void loadProjectiles() throws IOException {
        projectiles.putAll(projectileCacheLoader.readProjectilesFromCache());
        LOGGER.info("Loaded {} Projectiles.", projectiles.size());
    }

}
