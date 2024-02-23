package com.terryscape.game.combat;

import com.terryscape.cache.CacheLoader;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.task.step.Step;
import com.terryscape.world.coordinate.WorldCoordinate;
import com.terryscape.world.pathfinding.PathfindingManager;

import java.util.Arrays;

public class CombatFollowStep extends Step {
    private final PathfindingManager pathfindingManager;
    private final CacheLoader cacheLoader;
    private final MovementComponent attacker;
    private final MovementComponent victim;
    private WorldCoordinate lastTickVictimWorldCoordinate;
    private WorldCoordinate destinationTile;

    public CombatFollowStep(PathfindingManager pathfindingManager,
                            CacheLoader cacheLoader,
                            MovementComponent attacker,
                            MovementComponent victim) {

        this.pathfindingManager = pathfindingManager;
        this.cacheLoader = cacheLoader;
        this.attacker = attacker;
        this.victim = victim;
    }

    @Override
    public void tick() {
        super.tick();

        if (destinationTile == null || !lastTickVictimWorldCoordinate.equals(victim.getWorldCoordinate())) {
            destinationTile = calculateNewDestinationTile();
            attacker.move(destinationTile);
        }

        if (attacker.getWorldCoordinate().distance(victim.getWorldCoordinate()) < 3f) {
            attacker.face(victim);
        } else {
            attacker.stopFacing();
        }

        lastTickVictimWorldCoordinate = victim.getWorldCoordinate();
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void cancel() {
        attacker.stopFacing();
    }

    private WorldCoordinate calculateNewDestinationTile() {
        // TODO: Could do with a fresh look - feels like potentially alot of this is reusable and this shouldn't be working this stuff out
        // This is like generic interact with entity logic.

        var onSameTile = attacker.getWorldCoordinate().equals(victim.getWorldCoordinate());
        if (onSameTile) {
            return getWalkableNeighbourOnVictim();
        }

        var route = pathfindingManager.findRoute(attacker.getWorldCoordinate(), victim.getWorldCoordinate());
        if (route.isEmpty()) {
            failed();
            return null;
        }

        WorldCoordinate victimNeighbourCoordinate;
        if (route.get().size() <= 1) {
            victimNeighbourCoordinate = attacker.getWorldCoordinate();
        } else {
            victimNeighbourCoordinate = route.get().getWorldCoordinateFromEnd(1);
        }

        if (victim.getWorldCoordinate().isCardinal(victimNeighbourCoordinate)) {
            return victimNeighbourCoordinate;
        }

        var cardinalToVictim = Arrays.stream(victimNeighbourCoordinate.getCardinalNeighbours())
            .filter(wc -> wc.isCardinal(victim.getWorldCoordinate()))
            .toList();

        return WorldCoordinate.getClosestWorldCoordinate(attacker.getWorldCoordinate(), cardinalToVictim);
    }

    private WorldCoordinate getWalkableNeighbourOnVictim() {
        var cardinalNeighbours = victim.getWorldCoordinate().getCardinalNeighbours();

        for (WorldCoordinate cardinalNeighbour : cardinalNeighbours) {
            var region = cacheLoader.getWorldRegion(cardinalNeighbour.toWorldRegionCoordinate());
            var isWalkable = region.getWorldTileDefinition(cardinalNeighbour.toWorldRegionLocalCoordinate()).isWalkable();

            if (isWalkable) {
                return cardinalNeighbour;
            }
        }

        return victim.getWorldCoordinate().north();
    }
}
