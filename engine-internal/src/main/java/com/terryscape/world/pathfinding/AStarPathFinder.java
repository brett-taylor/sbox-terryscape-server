package com.terryscape.world.pathfinding;

import com.terryscape.world.Region;
import com.terryscape.world.coordinate.WorldCoordinate;

import java.util.*;

class AStarPathFinder {

    private final static float ADJACENT_TILE_COST = 1f;
    private final static float DIAGONAL_TILE_COST = 1.41421356237f;

    private final Region region;

    private final WorldCoordinate startingTile;

    private final WorldCoordinate destinationTile;

    private final PriorityQueue<PathfindingNode> openSet;

    private final PriorityQueue<PathfindingNode> closedSet;

    private final HashMap<PathfindingNode, Float> totalScores;

    private PathfindingNode current;

    AStarPathFinder(Region region, WorldCoordinate startingTile, WorldCoordinate destinationTile) {
        this.region = region;
        this.startingTile = startingTile;
        this.destinationTile = destinationTile;

        openSet = new PriorityQueue<>();
        closedSet = new PriorityQueue<>();
        totalScores = new HashMap<>();
    }

    // TODO We should probably verify the starting and ending tiles are walkable
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
        // TODO MAKE CHILDREN GETTING NICER

        var list = new ArrayList<PathfindingNode>();

        var north = current.getWorldCoordinate().north();
        var south = current.getWorldCoordinate().south();
        var east = current.getWorldCoordinate().east();
        var west = current.getWorldCoordinate().west();

        var isNorthValid = isValidWorldCoordinate(north);
        if (isNorthValid) {
            var node = new PathfindingNode(north).setParent(current);
            list.add(node);
        }

        var isEastValid = isValidWorldCoordinate(east);
        if (isEastValid) {
            var node = new PathfindingNode(east).setParent(current);
            list.add(node);
        }

        var isSouthValid = isValidWorldCoordinate(south);
        if (isSouthValid) {
            var node = new PathfindingNode(south).setParent(current);
            list.add(node);
        }

        var isWestValid = isValidWorldCoordinate(west);
        if (isWestValid) {
            var node = new PathfindingNode(west).setParent(current);
            list.add(node);
        }

        if (isNorthValid && isEastValid) {
            var northEast = current.getWorldCoordinate().northEast();
            if (isValidWorldCoordinate(northEast)) {
                var node = new PathfindingNode(northEast).setParent(current);
                list.add(node);
            }
        }

        if (isEastValid && isSouthValid) {
            var southEast = current.getWorldCoordinate().southEast();
            if (isValidWorldCoordinate(southEast)) {
                var node = new PathfindingNode(southEast).setParent(current);
                list.add(node);
            }
        }

        if (isSouthValid && isWestValid) {
            var southWest = current.getWorldCoordinate().southWest();
            if (isValidWorldCoordinate(southWest)) {
                var node = new PathfindingNode(southWest).setParent(current);
                list.add(node);
            }
        }

        if (isWestValid && isNorthValid) {
            var northWest = current.getWorldCoordinate().northWest();
            if (isValidWorldCoordinate(northWest)) {
                var node = new PathfindingNode(northWest).setParent(current);
                list.add(node);
            }
        }

        return list;
    }

    // TODO: This should probably refer to a WorldRegion::isValidCoordinate or something
    @Deprecated
    private boolean isValidWorldCoordinate(WorldCoordinate worldCoordinate) {
         var validX = worldCoordinate.getX() >= -30 && worldCoordinate.getX() < 60;
         var validY = worldCoordinate.getY() >= -30 && worldCoordinate.getY() < 60;

         if (!validX || !validY) {
             return false;
         }

        // TODO: Check coordinate is actually valid

        return region.isWalkable(worldCoordinate.getX(), worldCoordinate.getY());
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
