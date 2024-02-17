package com.terryscape.cache.world;

import com.terryscape.world.coordinate.WorldRegionLocalCoordinate;

import java.util.HashMap;

public class WorldRegionDefinitionImpl implements WorldRegionDefinition {

    private HashMap<WorldRegionLocalCoordinate, WorldTileDefinitionImpl> tiles;

    private HashMap<String, WorldObjectDefinitionImpl> objects;

    @Override
    public WorldTileDefinition getWorldTileDefinition(WorldRegionLocalCoordinate worldRegionLocalCoordinate) {
        if (tiles.containsKey(worldRegionLocalCoordinate)) {
            return tiles.get(worldRegionLocalCoordinate);
        }

        throw new IllegalArgumentException("%s is not a valid tile".formatted(worldRegionLocalCoordinate));
    }

    public WorldRegionDefinitionImpl setTiles(HashMap<WorldRegionLocalCoordinate, WorldTileDefinitionImpl> tiles) {
        this.tiles = tiles;
        return this;
    }

    @Override
    public WorldObjectDefinition getWorldObjectDefinition(String worldObjectId) {
        if (objects.containsKey(worldObjectId)) {
            return objects.get(worldObjectId);
        }

        throw new IllegalArgumentException("World Object with id %s does not exist".formatted(worldObjectId));
    }

    public WorldRegionDefinitionImpl setObjects(HashMap<String, WorldObjectDefinitionImpl> objects) {
        this.objects = objects;
        return this;
    }
}
