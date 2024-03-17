package com.terryscape.game.combat;

import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.world.coordinate.WorldCoordinate;
import com.terryscape.game.world.pathfinding.PathfindingManager;

import java.util.Objects;

public class CombatFollow {

    private final PathfindingManager pathfindingManager;

    private final CombatComponentImpl attacker;

    private final CombatComponentImpl victim;

    private WorldCoordinate lastTickVictimWorldCoordinate;

    private WorldCoordinate destinationTile;

    public CombatFollow(PathfindingManager pathfindingManager, CombatComponentImpl attacker, CombatComponentImpl victim) {
        this.pathfindingManager = pathfindingManager;

        this.attacker = attacker;
        this.victim = victim;
    }

    public void stop() {
        var attackerMovement = attacker.getEntity().getComponentOrThrow(MovementComponent.class);
        attackerMovement.stopFacing();
    }

    public CombatFollowResult tick() {
        var attackerMovement = attacker.getEntity().getComponentOrThrow(MovementComponent.class);
        var victimMovement = victim.getEntity().getComponentOrThrow(MovementComponent.class);

        attackerMovement.face(victimMovement);

        clearDestinationTileIfVictimMoved(victimMovement);

        var inRange = calculateIfInRange(attackerMovement, victimMovement);
        var inRangeAndHasLineOfSight = inRange && pathfindingManager.hasLineOfSight(attackerMovement.getWorldCoordinate(), victimMovement.getWorldCoordinate());
        var isMoving = destinationTile != null;

        if (inRangeAndHasLineOfSight) {
            destinationTile = null;
            attackerMovement.stop();
        }

        var onSameTile = Objects.equals(attackerMovement.getWorldCoordinate(), victimMovement.getWorldCoordinate());
        if (onSameTile || (!inRangeAndHasLineOfSight && !isMoving)) {
            var newDestinationTile = pathfindingManager.getClosestNeighbourToDestination(attackerMovement.getWorldCoordinate(), victimMovement.getWorldCoordinate());
            if (newDestinationTile.isEmpty()) {
                return CombatFollowResult.stopAttacking();
            }

            destinationTile = newDestinationTile.get();
            attackerMovement.move(destinationTile);
        }

        lastTickVictimWorldCoordinate = victim.getEntity().getComponentOrThrow(MovementComponent.class).getWorldCoordinate();

        return inRangeAndHasLineOfSight ? CombatFollowResult.isInRangeAndLineOfSight() : CombatFollowResult.notInRangeOrLineOfSight();
    }

    private void clearDestinationTileIfVictimMoved(MovementComponent victimMovement) {
        if (Objects.equals(lastTickVictimWorldCoordinate, victimMovement.getWorldCoordinate())) {
            return;
        }

        destinationTile = null;
    }

    private boolean calculateIfInRange(MovementComponent attackerMovement, MovementComponent victimMovement) {
        var attackerCombatRange = attacker.getCombatScript().range();
        return attackerMovement.getWorldCoordinate().tileDistance(victimMovement.getWorldCoordinate()) <= attackerCombatRange;
    }
}
