package com.terryscape.cache.world;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Singleton;
import com.terryscape.Config;
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

    public Map<WorldRegionCoordinate, WorldRegionDefinitionImpl> readWorldRegionsFromCache() throws IOException {
        var worldRegions = new HashMap<WorldRegionCoordinate, WorldRegionDefinitionImpl>();

        var worldRegionResources = getAllWorldRegionResources();
        worldRegionResources.forEach(resource -> worldRegions.put(
            getWorldRegionCoordinateFromRegionFile(resource.getFilename()),
            createWorldRegionDefinition(resource)
        ));

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

    private WorldRegionDefinitionImpl createWorldRegionDefinition(Resource resource) {
        var tileFlags = getTileFlagsJsonObjectFromResource(resource);
        var tiles = new HashMap<WorldRegionLocalCoordinate, WorldTileDefinitionImpl>();

        for (var entrySet : tileFlags.entrySet()) {
            var worldRegionLocalCoordinate = toWorldRegionLocalCoordinate(entrySet.getKey());
            var WorldTileDefinition = new WorldTileDefinitionImpl()
                .setWalkable(entrySet.getValue().getAsJsonObject().getAsJsonPrimitive("IsWalkable").getAsBoolean());

            tiles.put(worldRegionLocalCoordinate, WorldTileDefinition);
        }

        return new WorldRegionDefinitionImpl().setTiles(tiles);
    }

    private JsonObject getTileFlagsJsonObjectFromResource(Resource resource) {
        try {
            var jsonInput = resource.getContentAsString(StandardCharsets.UTF_8);
            return JsonParser.parseString(jsonInput).getAsJsonObject().get("TilesFlags").getAsJsonObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private WorldRegionLocalCoordinate toWorldRegionLocalCoordinate(String string) {
        var nameParts = string.split("-");

        return new WorldRegionLocalCoordinate(
            Integer.parseInt(nameParts[0]),
            Integer.parseInt(nameParts[1])
        );
    }

}
