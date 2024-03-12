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

    @Inject
    public PathfindingManagerImpl(CacheLoader cacheLoader) {
        this.cacheLoader = cacheLoader;
    }

    @Override
    public Optional<PathfindingRoute> findRoute(WorldCoordinate startingTile, WorldCoordinate destinationTile) {
        var stopwatch = Stopwatch.createStarted();

        var optionalPath = pathfind(startingTile, destinationTile, PathfindType.MOB);

        if (optionalPath.isPresent()) {
            var tickTimeMs = stopwatch.elapsed(TimeUnit.MILLISECONDS);
            var level = tickTimeMs > Config.SLOW_PATHFIND_THRESHOLD_MS ? Level.WARN : Level.DEBUG;
            LOGGER.log(level, "Successfully found a navigation path in {}ms from {} to {}.", stopwatch.elapsed(TimeUnit.MILLISECONDS), startingTile, destinationTile);
            return Optional.of(new PathfindingRouteImpl(optionalPath.get()));
        } else {
            LOGGER.warn("Failed to find a navigation path in {}ms from {} to {}.", stopwatch.elapsed(TimeUnit.MILLISECONDS), startingTile, destinationTile);
            return Optional.empty();
        }
    }

    @Override
    public boolean hasLineOfSight(WorldCoordinate startingTile, WorldCoordinate destinationTile) {
        //  var projectilePathfind = pathfind(startingTile, destinationTile, PathfindType.PROJECTILE);
        //  var mobPathfind = pathfind(startingTile, destinationTile, PathfindType.MOB);
        //  if (projectilePathfind.isEmpty() || mobPathfind.isEmpty()) {
        //    return false;
        //  }
        //  return Objects.equals(projectilePathfind.get(), mobPathfind.get());

        var optionalPath = pathfind(startingTile, destinationTile, PathfindType.PROJECTILE);
        if (optionalPath.isEmpty()) {
            return false;
        }

        for (WorldCoordinate worldCoordinate : optionalPath.get()) {
            var isWalkable = cacheLoader
                .getWorldRegionDefinition(worldCoordinate.toWorldRegionCoordinate())
                .getWorldTileDefinition(worldCoordinate.toWorldRegionLocalCoordinate())
                .isWalkable();

            if (!isWalkable) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Optional<WorldCoordinate> getClosestNeighbourToDestination(WorldCoordinate startingTile, WorldCoordinate destinationTile) {
        var onSameTile = startingTile.equals(destinationTile);
        if (onSameTile) {
            return Optional.of(getWalkableNeighbour(destinationTile));
        }

        var route = findRoute(startingTile, destinationTile);
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

    private WorldCoordinate getWalkableNeighbour(WorldCoordinate worldCoordinate) {
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

    private Optional<ArrayList<WorldCoordinate>> pathfind(WorldCoordinate startingTile, WorldCoordinate destinationTile, PathfindType pathfindType) {
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

        var pathfinder = new AStarPathFinder(startingTile, destinationTile, cacheLoader, pathfindType);
        return pathfinder.find();
    }
}
