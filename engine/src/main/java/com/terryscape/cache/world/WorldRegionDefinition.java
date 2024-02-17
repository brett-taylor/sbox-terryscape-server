package com.terryscape.cache.world;

import com.terryscape.world.coordinate.WorldRegionLocalCoordinate;

public interface WorldRegionDefinition {

    WorldTileDefinition getWorldTileDefinition(WorldRegionLocalCoordinate worldRegionLocalCoordinate);

    WorldObjectDefinition getWorldObjectDefinition(String worldObjectId);

}
