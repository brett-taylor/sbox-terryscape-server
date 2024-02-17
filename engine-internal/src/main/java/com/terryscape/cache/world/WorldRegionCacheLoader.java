package com.terryscape.cache.world;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.Config;
import com.terryscape.cache.CacheLoader;
import com.terryscape.cache.object.ObjectDefinitionImpl;
import com.terryscape.world.coordinate.WorldRealPosition;
import com.terryscape.world.coordinate.WorldRegionCoordinate;
import com.terryscape.world.coordinate.WorldRegionLocalCoordinate;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class WorldRegionCacheLoader {

    private final CacheLoader cacheLoader;

    @Inject
    public WorldRegionCacheLoader(CacheLoader cacheLoader) {
        this.cacheLoader = cacheLoader;
    }

    public Map<WorldRegionCoordinate, WorldRegionDefinitionImpl> readWorldRegionsFromCache() throws IOException {
        var worldRegions = new HashMap<WorldRegionCoordinate, WorldRegionDefinitionImpl>();

        var worldRegionResources = getAllWorldRegionResources();
        for (var resource : worldRegionResources) {
            var worldRegionCoordinate = getWorldRegionCoordinateFromRegionFile(resource.getFilename());
            var worldRegionDefinition = createWorldRegionDefinition(worldRegionCoordinate, resource);

            worldRegions.put(worldRegionCoordinate, worldRegionDefinition);
        }

        return worldRegions;
    }

    private List<Resource> getAllWorldRegionResources() throws IOException {
        var resolver = new PathMatchingResourcePatternResolver();
        var resources = resolver.getResources("%s/*.json".formatted(Config.WORLD_REGION_CACHE_LOCATION_DIRECTORY));
        return Arrays.stream(resources).toList();
    }

    private WorldRegionCoordinate getWorldRegionCoordinateFromRegionFile(String worldRegionFileName) {
        var fileName = worldRegionFileName.replace(".json", "");
        var nameParts = fileName.split(",");

        return new WorldRegionCoordinate(
            Integer.parseInt(nameParts[0]),
            Integer.parseInt(nameParts[1])
        );
    }

    private WorldRegionDefinitionImpl createWorldRegionDefinition(WorldRegionCoordinate worldRegionCoordinate, Resource resource) {
        var jsonObject = getJsonObjectFromResource(resource);
        var worldRegionDefinition = new WorldRegionDefinitionImpl();

        loadAndSetTileFlags(worldRegionDefinition, jsonObject);
        loadAndSetObjects(worldRegionCoordinate, worldRegionDefinition, jsonObject);

        return worldRegionDefinition;
    }

    private WorldRegionLocalCoordinate toWorldRegionLocalCoordinate(String string) {
        var nameParts = string.split("-");

        return new WorldRegionLocalCoordinate(
            Integer.parseInt(nameParts[0]),
            Integer.parseInt(nameParts[1])
        );
    }

    private JsonObject getJsonObjectFromResource(Resource resource) {
        try {
            var jsonInput = resource.getContentAsString(StandardCharsets.UTF_8);
            return JsonParser.parseString(jsonInput).getAsJsonObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadAndSetTileFlags(WorldRegionDefinitionImpl worldRegionDefinition, JsonObject jsonObject) {
        var tileFlags = jsonObject.get("TilesFlags").getAsJsonObject();
        var tilesToSet = new HashMap<WorldRegionLocalCoordinate, WorldTileDefinitionImpl>();

        for (var entrySet : tileFlags.entrySet()) {
            var worldRegionLocalCoordinate = toWorldRegionLocalCoordinate(entrySet.getKey());

            var worldTileDefinition = new WorldTileDefinitionImpl()
                .setWalkable(entrySet.getValue().getAsJsonObject().getAsJsonPrimitive("IsWalkable").getAsBoolean());

            tilesToSet.put(worldRegionLocalCoordinate, worldTileDefinition);
        }

        worldRegionDefinition.setTiles(tilesToSet);
    }

    private void loadAndSetObjects(WorldRegionCoordinate worldRegionCoordinate, WorldRegionDefinitionImpl worldRegionDefinition, JsonObject jsonObject) {
        var objectsJson = jsonObject.get("Objects").getAsJsonArray();
        var objectsToSet = new HashMap<String, WorldObjectDefinitionImpl>();

        for (var object : objectsJson) {
            var worldObjectId = object.getAsJsonObject().getAsJsonPrimitive("Id").getAsString();

            var objectDefinitionId = object.getAsJsonObject().getAsJsonPrimitive("ObjectDefinitionId").getAsString();
            var objectDefinition = (ObjectDefinitionImpl) cacheLoader.getObjectDefinition(objectDefinitionId);

            var positionJsonString = object.getAsJsonObject().getAsJsonPrimitive("Position").getAsString();
            var realPosition = WorldRealPosition.fromJsonString(positionJsonString);

            // The position in the cache is stored relative to the region, we must mae it an absolute world coordinate.
            var worldCoordinate = realPosition.asWorldRegionLocalCoordinate().toWorldCoordinate(worldRegionCoordinate);

            var worldObjectDefinition = new WorldObjectDefinitionImpl()
                .setObjectDefinition(objectDefinition)
                .setWorldCoordinate(worldCoordinate);

            objectsToSet.put(worldObjectId, worldObjectDefinition);
        }

        worldRegionDefinition.setObjects(objectsToSet);
    }

}