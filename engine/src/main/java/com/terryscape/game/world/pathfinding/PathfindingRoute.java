package com.terryscape.game.world.pathfinding;

import com.terryscape.game.world.coordinate.WorldCoordinate;

public interface PathfindingRoute {

    boolean hasNextWorldCoordinate();

    WorldCoordinate getNextWorldCoordinate();

    int size();

    int remaining();

    WorldCoordinate getWorldCoordinateFromEnd(int fromEnd);
}
