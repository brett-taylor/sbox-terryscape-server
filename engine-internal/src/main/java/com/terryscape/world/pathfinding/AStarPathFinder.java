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

    private final PathfindType pathfindType;

    private PathfindingNode current;

    AStarPathFinder(WorldCoordinate startingTile, WorldCoordinate destinationTile, CacheLoader cacheLoader, PathfindType pathfindType) {
        this.startingTile = startingTile;
        this.destinationTile = destinationTile;
        this.cacheLoader = cacheLoader;
        this.pathfindType = pathfindType;

        openSet = new PriorityQueue<>();
        closedSet = new PriorityQueue<>();
        totalScores = new HashMap<>();
    }

    public Optional<ArrayList<WorldCoordinate>> find() {
        var start = new PathfindingNode(startingTile);
        addNodeToOpenSet(start);

        doAStar();

        if (current.getWorldCoordinate().equals(destinationTile)) {
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
            if (current.getWorldCoordinate().equals(destinationTile)) {
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
        var walkableNeighbours = Arrays.stream(current.getWorldCoordinate().getCardinalAndIntercardinalNeighbours())
            .filter(this::isValidWorldCoordinate)
            .toList();

        return walkableNeighbours.stream()
            .filter(worldCoordinate -> isValidDiagonalTile(worldCoordinate, walkableNeighbours))
            .map(worldCoordinate -> new PathfindingNode(worldCoordinate).setParent(current))
            .toList();
    }

    private boolean isValidWorldCoordinate(WorldCoordinate worldCoordinate) {
        if (pathfindType == PathfindType.PROJECTILE) {
            return true;
        }

        var regionCoordinate = worldCoordinate.toWorldRegionCoordinate();
        var localCoordinate = worldCoordinate.toWorldRegionLocalCoordinate();
        var region = cacheLoader.getWorldRegionDefinitionSafe(regionCoordinate);

        return region
            .map(worldRegionDefinition -> worldRegionDefinition.getWorldTileDefinition(localCoordinate)
            .isWalkable())
            .orElse(false);
    }

    private boolean isValidDiagonalTile(WorldCoordinate worldCoordinate, List<WorldCoordinate> validNeighbours) {
        var currentWc = current.getWorldCoordinate();
        var direction = currentWc.directionTo(worldCoordinate);

        return switch (direction) {
            case NORTH, EAST, SOUTH, WEST -> true;
            case NORTH_EAST -> validNeighbours.contains(currentWc.north()) && validNeighbours.contains(currentWc.east());
            case SOUTH_EAST -> validNeighbours.contains(currentWc.south()) && validNeighbours.contains(currentWc.east());
            case SOUTH_WEST -> validNeighbours.contains(currentWc.south()) && validNeighbours.contains(currentWc.west());
            case NORTH_WEST -> validNeighbours.contains(currentWc.north()) && validNeighbours.contains(currentWc.west());
        };
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
