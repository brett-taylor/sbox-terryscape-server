package com.terryscape.world.pathfinding;

import com.google.common.base.Stopwatch;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.CacheLoader;
import com.terryscape.world.coordinate.WorldCoordinate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Singleton
public class PathfindingManagerImpl implements PathfindingManager {

    private static final Logger LOGGER = LogManager.getLogger(PathfindingManagerImpl.class);

    private final CacheLoader cacheLoader;

    @Inject
    public PathfindingManagerImpl(CacheLoader cacheLoader) {
        this.cacheLoader = cacheLoader;
    }

    @Override
    public Optional<PathfindingRoute> findRoute(WorldCoordinate startingTile, WorldCoordinate destinationTile) {
        var stopwatch = Stopwatch.createStarted();

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

        var pathfinder = new AStarPathFinder(startingTile, destinationTile, cacheLoader);
        var optionalPath = pathfinder.find();

        if (optionalPath.isPresent()) {
            // TODO: Log slow pathfinds
            LOGGER.debug("Successfully found a navigation path in {}ms from {} to {}.", stopwatch.elapsed(TimeUnit.MILLISECONDS), startingTile, destinationTile);
            return Optional.of(new PathfindingRouteImpl(optionalPath.get()));
        } else {
            LOGGER.warn("Failed to find a navigation path in {}ms from {} to {}.", stopwatch.elapsed(TimeUnit.MILLISECONDS), startingTile, destinationTile);
            return Optional.empty();
        }
    }
}
