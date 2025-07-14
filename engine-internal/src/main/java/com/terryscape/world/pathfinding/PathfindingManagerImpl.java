package com.terryscape.world.pathfinding;

import com.google.common.base.Stopwatch;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.Config;
import com.terryscape.cache.CacheLoader;
import com.terryscape.maths.RandomUtil;
import com.terryscape.world.coordinate.WorldCoordinate;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Singleton
public class PathfindingManagerImpl implements PathfindingManager {

    private static final Logger LOGGER = LogManager.getLogger(PathfindingManagerImpl.class);

    private final CacheLoader cacheLoader;
    private final PathfindingCache pathfindingCache;

    @Inject
    public PathfindingManagerImpl(CacheLoader cacheLoader, PathfindingCache pathfindingCache) {
        this.cacheLoader = cacheLoader;
        this.pathfindingCache = pathfindingCache;
    }

    @Override
    public Optional<PathfindingRoute> findRoute(WorldCoordinate startingTile, WorldCoordinate destinationTile, int agentSize) {
        var stopwatch = Stopwatch.createStarted();

        var optionalPath = pathfind(startingTile, destinationTile, PathfindType.MOB, agentSize);

        if (optionalPath.isPresent()) {
            var tickTimeMs = stopwatch.elapsed(TimeUnit.MILLISECONDS);
            var level = tickTimeMs > Config.SLOW_PATHFIND_THRESHOLD_MS ? Level.WARN : Level.DEBUG;
            LOGGER.log(level, "Successfully found a navigation path in {}ms from {} to {} with agent size {}.", stopwatch.elapsed(TimeUnit.MILLISECONDS), startingTile, destinationTile, agentSize);
            return Optional.of(new PathfindingRouteImpl(optionalPath.get()));
        } else {
            LOGGER.warn("Failed to find a navigation path in {}ms from {} to {} with agent size {}.", stopwatch.elapsed(TimeUnit.MILLISECONDS), startingTile, destinationTile, agentSize);
            return Optional.empty();
        }
    }

    @Override
    public boolean hasLineOfSight(WorldCoordinate startingTile, WorldCoordinate destinationTile, int agentSize) {
        // Always use agent size 1 for line of sight checks
        var projectilePathfind = pathfind(startingTile, destinationTile, PathfindType.PROJECTILE, 1);
        var mobPathfind = pathfind(startingTile, destinationTile, PathfindType.MOB, 1);
        if (projectilePathfind.isEmpty() || mobPathfind.isEmpty()) {
            return false;
        }

        return Objects.equals(projectilePathfind.get(), mobPathfind.get());
    }

    @Override
    public Optional<WorldCoordinate> getClosestNeighbourToDestination(WorldCoordinate startingTile, WorldCoordinate destinationTile, int agentSize) {
        var onSameTile = startingTile.equals(destinationTile);
        if (onSameTile) {
            return Optional.of(getWalkableNeighbour(destinationTile, agentSize));
        }

        var route = findRoute(startingTile, destinationTile, agentSize);
        if (route.isEmpty()) {
            return Optional.empty();
        }

        WorldCoordinate neighbourWorldCoordinateToDestinationWorldCoordinate;
        if (route.get().size() <= 1) {
            neighbourWorldCoordinateToDestinationWorldCoordinate = startingTile;
        } else {
            neighbourWorldCoordinateToDestinationWorldCoordinate = route.get().getWorldCoordinateFromEnd(1);
        }

        if (destinationTile.isCardinal(neighbourWorldCoordinateToDestinationWorldCoordinate)) {
            return Optional.of(neighbourWorldCoordinateToDestinationWorldCoordinate);
        }

        var cardinalToVictim = Arrays.stream(neighbourWorldCoordinateToDestinationWorldCoordinate.getCardinalNeighbours())
            .filter(wc -> wc.isCardinal(destinationTile))
            .toList();

        return Optional.of(WorldCoordinate.getClosestWorldCoordinate(startingTile, cardinalToVictim));
    }

    private WorldCoordinate getWalkableNeighbour(WorldCoordinate worldCoordinate, int agentSize) {
        var cardinalNeighbours = worldCoordinate.getCardinalNeighbours();

        var validCardinalNeighbours = Arrays.stream(cardinalNeighbours).filter(cardinalNeighbour -> {
            var region = cacheLoader.getWorldRegionDefinition(cardinalNeighbour.toWorldRegionCoordinate());
            return region.getWorldTileDefinition(cardinalNeighbour.toWorldRegionLocalCoordinate()).isWalkable();
        }).toList();

        if (validCardinalNeighbours.isEmpty()) {
            return worldCoordinate.north();
        } else {
            return RandomUtil.randomCollection(validCardinalNeighbours);
        }
    }

    private Optional<ArrayList<WorldCoordinate>> pathfind(WorldCoordinate startingTile, WorldCoordinate destinationTile, PathfindType pathfindType, int agentSize) {
        var isStartTileWalkable = cacheLoader
            .getWorldRegionDefinition(startingTile.toWorldRegionCoordinate())
            .getWorldTileDefinition(startingTile.toWorldRegionLocalCoordinate())
            .isWalkable();

        var isDestinationTileWalkable = cacheLoader
            .getWorldRegionDefinition(destinationTile.toWorldRegionCoordinate())
            .getWorldTileDefinition(destinationTile.toWorldRegionLocalCoordinate())
            .isWalkable();

        if (!isStartTileWalkable || !isDestinationTileWalkable) {
            return Optional.empty();
        }

        var pathfinder = new AStarPathFinder(startingTile, destinationTile, cacheLoader, pathfindType, agentSize, pathfindingCache);
        return pathfinder.find();
    }
}
