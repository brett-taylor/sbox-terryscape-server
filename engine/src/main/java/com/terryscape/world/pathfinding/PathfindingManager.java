package com.terryscape.world.pathfinding;

import com.terryscape.world.coordinate.WorldCoordinate;

import java.util.Optional;

public interface PathfindingManager {

    Optional<PathfindingRoute> findRoute(WorldCoordinate startingTile, WorldCoordinate destinationTile, int agentSize);

    boolean hasLineOfSight(WorldCoordinate startingTile, WorldCoordinate destinationTile, int agentSize);

    Optional<WorldCoordinate> getClosestNeighbourToDestination(WorldCoordinate startingTile, WorldCoordinate destinationTile, int agentSize);
}
