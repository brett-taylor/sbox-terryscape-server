package com.terryscape.world.pathfinding;

import com.terryscape.world.WorldCoordinate;

import java.util.ArrayList;

public class PathfindingRouteImpl implements PathfindingRoute {

    private final ArrayList<WorldCoordinate> path;

    private int currentPosition = 0;

    public PathfindingRouteImpl(ArrayList<WorldCoordinate> path) {
        this.path = path;
    }

    @Override
    public boolean hasNextWorldCoordinate() {
        return currentPosition < path.size() - 1;
    }

    @Override
    public WorldCoordinate getNextWorldCoordinate() {
        if (!hasNextWorldCoordinate()) {
            return null;
        }

        currentPosition += 1;
        return path.get(currentPosition);
    }

    @Override
    public int size() {
        return path.size();
    }

    @Override
    public WorldCoordinate getWorldCoordinateFromEnd(int fromEnd) {
        return path.get(size() - 1 - fromEnd);
    }
}