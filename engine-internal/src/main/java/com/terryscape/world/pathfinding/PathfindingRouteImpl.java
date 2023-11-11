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
    public boolean hasNextTile() {
        return currentPosition < path.size() - 1;
    }

    @Override
    public WorldCoordinate getNextTile() {
        if (!hasNextTile()) {
            return null;
        }

        currentPosition += 1;
        return path.get(currentPosition);
    }
}