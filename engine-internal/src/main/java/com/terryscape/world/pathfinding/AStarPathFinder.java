package com.terryscape.world.pathfinding;

import com.terryscape.cache.CacheLoader;
import com.terryscape.world.coordinate.WorldCoordinate;

import java.util.*;

class AStarPathFinder {

    private final WorldCoordinate startingTile;

    private final WorldCoordinate destinationTile;

    private final CacheLoader cacheLoader;

    private final PriorityQueue<PathfindingNode> openSet;

    private final PriorityQueue<PathfindingNode> closedSet;

    private final HashMap<PathfindingNode, Float> totalScores;
    private final int distance;

    private PathfindingNode current;

    AStarPathFinder(WorldCoordinate startingTile, WorldCoordinate destinationTile, CacheLoader cacheLoader) {
        this.startingTile = startingTile;
        this.destinationTile = destinationTile;
        this.cacheLoader = cacheLoader;
        distance = 0;

        openSet = new PriorityQueue<>();
        closedSet = new PriorityQueue<>();
        totalScores = new HashMap<>();
    }

    AStarPathFinder(WorldCoordinate startingTile, WorldCoordinate destinationTile, CacheLoader cacheLoader, int distance) {
        this.startingTile = startingTile;
        this.destinationTile = destinationTile;
        this.cacheLoader = cacheLoader;
        this.distance = distance;

        openSet = new PriorityQueue<>();
        closedSet = new PriorityQueue<>();
        totalScores = new HashMap<>();
    }

    public Optional<ArrayList<WorldCoordinate>> find() {
        var start = new PathfindingNode(startingTile);
        addNodeToOpenSet(start);

        doAStar();

        if (current.getWorldCoordinate().distance(destinationTile) <= distance) {
            return Optional.of(generatePath());
        } else {
            return Optional.empty();
        }
    }

    private void doAStar() {
        while (!openSet.isEmpty()) {
            current = openSet.poll();
            addNodeToClosedSet(current);

            // If we found our destination lets stop
            if (current.getWorldCoordinate().distance(destinationTile) <= distance) {
                return;
            }

            var neighbours = getValidNeighbours();
            neighbours.stream()
                .filter(this::shouldAddNeighbourToOpenList)
                .forEach(this::addNodeToOpenSet);
        }
    }

    private void addNodeToOpenSet(PathfindingNode node) {
        openSet.add(node);
        totalScores.put(node, node.getTotalCost());
    }

    private void addNodeToClosedSet(PathfindingNode node) {
        closedSet.add(node);
        totalScores.remove(node);
    }

    private List<PathfindingNode> getValidNeighbours() {
        return Arrays.stream(current.getWorldCoordinate().getCardinalAndIntercardinalNeighbours())
            .filter(this::isValidWorldCoordinate)
            .map(worldCoordinate -> new PathfindingNode(worldCoordinate).setParent(current))
            .toList();
    }

    private boolean isValidWorldCoordinate(WorldCoordinate worldCoordinate) {
        var region = cacheLoader.getWorldRegion(worldCoordinate.toWorldRegionCoordinate());
        return region.getWorldTileDefinition(worldCoordinate.toWorldRegionLocalCoordinate()).isWalkable();
    }

    private boolean shouldAddNeighbourToOpenList(PathfindingNode neighbour) {
        if (closedSet.contains(neighbour)) {
            return false;
        }

        var costToNewNeighbour = current.getWorldCoordinate().distance(neighbour.getWorldCoordinate());
        var newDistanceToStartNode = current.getDistanceToStartNode() + costToNewNeighbour;
        neighbour.setDistanceToStartNode(newDistanceToStartNode);

        var newDistanceToEndNode = heuristic(neighbour.getWorldCoordinate(), destinationTile);
        neighbour.setDistanceToEndNode(newDistanceToEndNode);

        if (openSet.contains(neighbour)) {
            return neighbour.getTotalCost() < totalScores.get(neighbour);
        } else {
            return true;
        }
    }

    private float heuristic(WorldCoordinate a, WorldCoordinate b) {
        return a.distance(b);
    }

    private ArrayList<WorldCoordinate> generatePath() {
        var resultList = new ArrayList<WorldCoordinate>();

        while (current != null) {
            resultList.add(current.getWorldCoordinate());
            current = current.getParent();
        }

        Collections.reverse(resultList);
        return resultList;
    }

}
