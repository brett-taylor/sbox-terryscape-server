package com.terryscape.game.world.pathfinding;

import com.terryscape.game.world.coordinate.WorldCoordinate;

import java.util.Optional;

public interface PathfindingManager {

    Optional<PathfindingRoute> findRoute(WorldCoordinate startingTile, WorldCoordinate destinationTile);

    boolean hasLineOfSight(WorldCoordinate startingTile, WorldCoordinate destinationTile);

    Optional<WorldCoordinate> getClosestNeighbourToDestination(WorldCoordinate startingTile, WorldCoordinate destinationTile);
}
