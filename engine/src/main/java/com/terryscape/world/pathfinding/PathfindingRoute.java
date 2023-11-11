package com.terryscape.world.pathfinding;

import com.terryscape.world.WorldCoordinate;

public interface PathfindingRoute {

    boolean hasNextTile();

    WorldCoordinate getNextTile();
}
