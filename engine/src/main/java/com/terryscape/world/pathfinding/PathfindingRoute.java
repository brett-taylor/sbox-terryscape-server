package com.terryscape.world.pathfinding;

import com.terryscape.world.WorldCoordinate;

public interface PathfindingRoute {

    boolean hasNextWorldCoordinate();

    WorldCoordinate getNextWorldCoordinate();

    int size();

    int remaining();

    WorldCoordinate getWorldCoordinateFromEnd(int fromEnd);
}
