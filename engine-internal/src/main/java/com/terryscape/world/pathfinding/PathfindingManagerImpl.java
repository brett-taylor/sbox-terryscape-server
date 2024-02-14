package com.terryscape.world.pathfinding;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.world.Region;
import com.terryscape.world.coordinate.WorldCoordinate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

@Singleton
public class PathfindingManagerImpl implements PathfindingManager {

    private static final Logger LOGGER = LogManager.getLogger(PathfindingManagerImpl.class);

    private final Region region;

    @Inject
    public PathfindingManagerImpl() {
        region = new Region();
    }

    @Override
    public Optional<PathfindingRoute> findRoute(WorldCoordinate startingTile, WorldCoordinate destinationTile) {
        var startTime = System.nanoTime();

        if (!region.isWalkable(destinationTile.getX(), destinationTile.getY())) {
            return Optional.empty();
        }

        var pathfinder = new AStarPathFinder(region, startingTile, destinationTile);
        var optionalPath = pathfinder.find();

        var timeTakenMicroSeconds = (System.nanoTime() - startTime) / 1000;
        if (optionalPath.isPresent()) {
            LOGGER.debug("Successfully found a navigation path in {} microseconds from {} to {}.", timeTakenMicroSeconds, startingTile, destinationTile);
            return Optional.of(new PathfindingRouteImpl(optionalPath.get()));
        } else {
            LOGGER.warn("Failed to find a navigation path in {} microseconds from {} to {}.", timeTakenMicroSeconds, startingTile, destinationTile);
            return Optional.empty();
        }
    }
}
