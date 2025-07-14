package com.terryscape.world.pathfinding;

import com.google.inject.Singleton;
import com.terryscape.world.coordinate.WorldCoordinate;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton cache for pathfinding operations.
 * Stores tile validity information based on agent size to avoid redundant calculations.
 */
@Singleton
public class PathfindingCache {
    
    // Cache for tile lookups based on agent size
    // Map<AgentSize, Map<WorldCoordinate, IsWalkable>>
    private final Map<Integer, Map<WorldCoordinate, Boolean>> tileValidityCache = new HashMap<>();
    
    /**
     * Checks if a tile's walkability is cached for the given agent size.
     *
     * @param agentSize the size of the agent
     * @param worldCoordinate the coordinate to check
     * @return true if the tile's walkability is cached, false otherwise
     */
    public boolean hasTileValidity(int agentSize, WorldCoordinate worldCoordinate) {
        return tileValidityCache.containsKey(agentSize) && 
               tileValidityCache.get(agentSize).containsKey(worldCoordinate);
    }
    
    /**
     * Gets the cached walkability of a tile for the given agent size.
     *
     * @param agentSize the size of the agent
     * @param worldCoordinate the coordinate to check
     * @return the cached walkability, or null if not cached
     */
    public Boolean getTileValidity(int agentSize, WorldCoordinate worldCoordinate) {
        if (!hasTileValidity(agentSize, worldCoordinate)) {
            return null;
        }
        return tileValidityCache.get(agentSize).get(worldCoordinate);
    }
    
    /**
     * Caches the walkability of a tile for the given agent size.
     *
     * @param agentSize the size of the agent
     * @param worldCoordinate the coordinate to cache
     * @param isValid whether the tile is walkable
     */
    public void cacheTileValidity(int agentSize, WorldCoordinate worldCoordinate, boolean isValid) {
        tileValidityCache.computeIfAbsent(agentSize, k -> new HashMap<>())
                        .put(worldCoordinate, isValid);
    }

}