package com.terryscape.game.world.pathfinding;

import com.terryscape.game.world.coordinate.WorldCoordinate;

import java.util.Objects;

public class PathfindingNode implements Comparable<PathfindingNode> {

    private final WorldCoordinate worldCoordinate;

    private PathfindingNode parent;

    private float distanceToStartNode;

    private float distanceToEndNode;

    public PathfindingNode(WorldCoordinate worldCoordinate) {
        this.worldCoordinate = worldCoordinate;
    }

    public WorldCoordinate getWorldCoordinate() {
        return worldCoordinate;
    }

    public PathfindingNode getParent() {
        return parent;
    }

    public PathfindingNode setParent(PathfindingNode parent) {
        this.parent = parent;
        return this;
    }

    public float getDistanceToStartNode() {
        return distanceToStartNode;
    }

    public PathfindingNode setDistanceToStartNode(float distanceToStartNode) {
        this.distanceToStartNode = distanceToStartNode;
        return this;
    }

    public float getDistanceToEndNode() {
        return distanceToEndNode;
    }

    public PathfindingNode setDistanceToEndNode(float distanceToEndNode) {
        this.distanceToEndNode = distanceToEndNode;
        return this;
    }

    public float getTotalCost() {
        return getDistanceToStartNode() + getDistanceToEndNode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PathfindingNode that = (PathfindingNode) o;
        return Objects.equals(getWorldCoordinate(), that.getWorldCoordinate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getWorldCoordinate());
    }

    @Override
    public int compareTo(PathfindingNode o) {
        if (this.getTotalCost() < o.getTotalCost()) {
            return -1;
        } else if (this.getTotalCost() == o.getTotalCost()) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public String toString() {
        return "PathfindingNode(worldCoordinate=%s, distanceToStartNode=%s, distanceToEndNode=%s, totalCost=%s)".formatted(
            worldCoordinate,
            distanceToStartNode,
            distanceToEndNode,
            getTotalCost()
        );
    }
}
