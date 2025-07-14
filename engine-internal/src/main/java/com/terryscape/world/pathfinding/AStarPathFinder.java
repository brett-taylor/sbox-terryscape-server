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

    private final int agentSize;

    private final PathfindingCache pathfindingCache;

    private final PriorityQueue<PathfindingNode> reverseOpenSet;

    private final PriorityQueue<PathfindingNode> reverseClosedSet;

    private final HashMap<PathfindingNode, Float> reverseTotalScores;

    private boolean searchingForward = true;

    private PathfindingNode reverseCurrent;

    private PathfindingNode current;

    AStarPathFinder(WorldCoordinate startingTile, WorldCoordinate destinationTile, CacheLoader cacheLoader, PathfindType pathfindType, int agentSize, PathfindingCache pathfindingCache) {
        this.startingTile = startingTile;
        this.destinationTile = destinationTile;
        this.cacheLoader = cacheLoader;
        this.pathfindType = pathfindType;
        this.agentSize = agentSize;
        this.pathfindingCache = pathfindingCache;

        openSet = new PriorityQueue<>();
        closedSet = new PriorityQueue<>();
        totalScores = new HashMap<>();

        // Initialize bidirectional search structures
        reverseOpenSet = new PriorityQueue<>();
        reverseClosedSet = new PriorityQueue<>();
        reverseTotalScores = new HashMap<>();
    }

    public Optional<ArrayList<WorldCoordinate>> find() {
        // Initialize forward search
        var start = new PathfindingNode(startingTile);
        addNodeToOpenSet(start);

        // Initialize reverse search
        var end = new PathfindingNode(destinationTile);
        addNodeToReverseOpenSet(end);

        // Meeting point for bidirectional search
        PathfindingNode meetingPoint = null;
        float bestPathCost = Float.MAX_VALUE;

        // Bidirectional A* search
        while (!openSet.isEmpty() && !reverseOpenSet.isEmpty()) {
            // Alternate between forward and reverse search
            if (searchingForward) {
                // Forward search step
                current = openSet.poll();
                addNodeToClosedSet(current);

                // Check if we've found a meeting point
                if (reverseClosedSet.contains(current)) {
                    // Found a potential meeting point
                    PathfindingNode reverseNode = findNodeInSet(reverseClosedSet, current.getWorldCoordinate());
                    float pathCost = current.getDistanceToStartNode() + reverseNode.getDistanceToStartNode();

                    if (pathCost < bestPathCost) {
                        meetingPoint = current;
                        bestPathCost = pathCost;
                    }
                }

                // If we found our destination directly, we're done
                if (current.getWorldCoordinate().equals(destinationTile)) {
                    return Optional.of(generatePath());
                }

                // Process neighbors
                var neighbours = getValidNeighbours(current);
                neighbours.stream()
                    .filter(n -> shouldAddNeighbourToOpenList(n, openSet, closedSet, totalScores))
                    .forEach(this::addNodeToOpenSet);
            } else {
                // Reverse search step
                reverseCurrent = reverseOpenSet.poll();
                addNodeToReverseClosedSet(reverseCurrent);

                // Check if we've found a meeting point
                if (closedSet.contains(reverseCurrent)) {
                    // Found a potential meeting point
                    PathfindingNode forwardNode = findNodeInSet(closedSet, reverseCurrent.getWorldCoordinate());
                    float pathCost = forwardNode.getDistanceToStartNode() + reverseCurrent.getDistanceToStartNode();

                    if (pathCost < bestPathCost) {
                        meetingPoint = forwardNode;
                        bestPathCost = pathCost;
                    }
                }

                // If we found our start directly, we're done
                if (reverseCurrent.getWorldCoordinate().equals(startingTile)) {
                    current = reverseCurrent;
                    return Optional.of(generateReversePath());
                }

                // Process neighbors
                var neighbours = getValidNeighbours(reverseCurrent);
                neighbours.stream()
                    .filter(n -> shouldAddNeighbourToOpenList(n, reverseOpenSet, reverseClosedSet, reverseTotalScores))
                    .forEach(this::addNodeToReverseOpenSet);
            }

            // Toggle search direction
            searchingForward = !searchingForward;
        }

        // If we found a meeting point, construct the path
        if (meetingPoint != null) {
            return Optional.of(generateBidirectionalPath(meetingPoint));
        }

        return Optional.empty();
    }

    private PathfindingNode findNodeInSet(PriorityQueue<PathfindingNode> set, WorldCoordinate coordinate) {
        for (PathfindingNode node : set) {
            if (node.getWorldCoordinate().equals(coordinate)) {
                return node;
            }
        }
        return null;
    }

    private void addNodeToOpenSet(PathfindingNode node) {
        openSet.add(node);
        totalScores.put(node, node.getTotalCost());
    }

    private void addNodeToReverseOpenSet(PathfindingNode node) {
        reverseOpenSet.add(node);
        reverseTotalScores.put(node, node.getTotalCost());
    }

    private void addNodeToClosedSet(PathfindingNode node) {
        closedSet.add(node);
        totalScores.remove(node);
    }

    private void addNodeToReverseClosedSet(PathfindingNode node) {
        reverseClosedSet.add(node);
        reverseTotalScores.remove(node);
    }

    private List<PathfindingNode> getValidNeighbours() {
        return getValidNeighbours(current);
    }

    private List<PathfindingNode> getValidNeighbours(PathfindingNode node) {
        var walkableNeighbours = Arrays.stream(node.getWorldCoordinate().getCardinalAndIntercardinalNeighbours())
            .filter(this::isValidWorldCoordinate)
            .toList();

        return walkableNeighbours.stream()
            .filter(worldCoordinate -> isValidDiagonalTile(worldCoordinate, walkableNeighbours, node))
            .map(worldCoordinate -> new PathfindingNode(worldCoordinate).setParent(node))
            .toList();
    }

    private boolean isValidDiagonalTile(WorldCoordinate worldCoordinate, List<WorldCoordinate> validNeighbours, PathfindingNode node) {
        var currentWc = node.getWorldCoordinate();
        var direction = currentWc.directionTo(worldCoordinate);

        return switch (direction) {
            case NORTH, EAST, SOUTH, WEST -> true;
            case NORTH_EAST -> validNeighbours.contains(currentWc.north()) && validNeighbours.contains(currentWc.east());
            case SOUTH_EAST -> validNeighbours.contains(currentWc.south()) && validNeighbours.contains(currentWc.east());
            case SOUTH_WEST -> validNeighbours.contains(currentWc.south()) && validNeighbours.contains(currentWc.west());
            case NORTH_WEST -> validNeighbours.contains(currentWc.north()) && validNeighbours.contains(currentWc.west());
        };
    }

    private boolean isValidWorldCoordinate(WorldCoordinate worldCoordinate) {
        if (pathfindType == PathfindType.PROJECTILE) {
            return true;
        }

        // Check cache first
        if (pathfindingCache.hasTileValidity(agentSize, worldCoordinate)) {
            return pathfindingCache.getTileValidity(agentSize, worldCoordinate);
        }

        // For agent size 1, just check if the tile is walkable
        if (agentSize == 1) {
            var regionCoordinate = worldCoordinate.toWorldRegionCoordinate();
            var localCoordinate = worldCoordinate.toWorldRegionLocalCoordinate();
            var region = cacheLoader.getWorldRegionDefinitionSafe(regionCoordinate);

            boolean isValid = region
                .map(worldRegionDefinition -> worldRegionDefinition.getWorldTileDefinition(localCoordinate)
                .isWalkable())
                .orElse(false);

            // Cache the result
            pathfindingCache.cacheTileValidity(agentSize, worldCoordinate, isValid);

            return isValid;
        }

        // For larger agent sizes, check all tiles in the agent's footprint
        boolean isValid = true;
        int radius = (agentSize - 1) / 2;

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                WorldCoordinate checkCoord = new WorldCoordinate(worldCoordinate.getX() + dx, worldCoordinate.getY() + dy);
                var regionCoordinate = checkCoord.toWorldRegionCoordinate();
                var localCoordinate = checkCoord.toWorldRegionLocalCoordinate();
                var region = cacheLoader.getWorldRegionDefinitionSafe(regionCoordinate);

                boolean tileValid = region
                    .map(worldRegionDefinition -> worldRegionDefinition.getWorldTileDefinition(localCoordinate)
                    .isWalkable())
                    .orElse(false);

                if (!tileValid) {
                    isValid = false;
                    break;
                }
            }
            if (!isValid) {
                break;
            }
        }

        // Cache the result
        pathfindingCache.cacheTileValidity(agentSize, worldCoordinate, isValid);

        return isValid;
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
        return shouldAddNeighbourToOpenList(neighbour, openSet, closedSet, totalScores);
    }

    private boolean shouldAddNeighbourToOpenList(
            PathfindingNode neighbour, 
            PriorityQueue<PathfindingNode> openSet, 
            PriorityQueue<PathfindingNode> closedSet,
            HashMap<PathfindingNode, Float> totalScores) {

        if (closedSet.contains(neighbour)) {
            return false;
        }

        PathfindingNode currentNode = searchingForward ? current : reverseCurrent;
        WorldCoordinate targetTile = searchingForward ? destinationTile : startingTile;

        var costToNewNeighbour = currentNode.getWorldCoordinate().distance(neighbour.getWorldCoordinate());
        var newDistanceToStartNode = currentNode.getDistanceToStartNode() + costToNewNeighbour;
        neighbour.setDistanceToStartNode(newDistanceToStartNode);

        var newDistanceToEndNode = heuristic(neighbour.getWorldCoordinate(), targetTile);
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

    private ArrayList<WorldCoordinate> generateReversePath() {
        var resultList = new ArrayList<WorldCoordinate>();

        while (reverseCurrent != null) {
            resultList.add(reverseCurrent.getWorldCoordinate());
            reverseCurrent = reverseCurrent.getParent();
        }

        return resultList;
    }

    private ArrayList<WorldCoordinate> generateBidirectionalPath(PathfindingNode meetingPoint) {
        var resultList = new ArrayList<WorldCoordinate>();

        // Get the forward path to the meeting point
        PathfindingNode node = meetingPoint;
        while (node != null) {
            resultList.add(node.getWorldCoordinate());
            node = node.getParent();
        }

        Collections.reverse(resultList);

        // Find the corresponding node in the reverse search
        PathfindingNode reverseNode = findNodeInSet(reverseClosedSet, meetingPoint.getWorldCoordinate());
        if (reverseNode != null) {
            // Skip the meeting point (already added) and add the rest of the reverse path
            reverseNode = reverseNode.getParent();
            while (reverseNode != null) {
                resultList.add(reverseNode.getWorldCoordinate());
                reverseNode = reverseNode.getParent();
            }
        }

        return resultList;
    }

}
