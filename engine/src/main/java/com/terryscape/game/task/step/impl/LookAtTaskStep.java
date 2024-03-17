package com.terryscape.game.task.step.impl;

import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.task.step.TaskStep;
import com.terryscape.game.world.coordinate.WorldCoordinate;

public class LookAtTaskStep extends TaskStep {

    public static LookAtTaskStep worldCoordinate(MovementComponent movementComponent, WorldCoordinate worldCoordinate) {
        return new LookAtTaskStep(movementComponent, worldCoordinate);
    }

    private final MovementComponent movementComponent;

    private final WorldCoordinate worldCoordinateToLookAt;

    private LookAtTaskStep(MovementComponent movementComponent, WorldCoordinate worldCoordinateToLookAt) {
        this.movementComponent = movementComponent;
        this.worldCoordinateToLookAt = worldCoordinateToLookAt;
    }

    @Override
    public void onBecameCurrentTaskStep() {
        super.onBecameCurrentTaskStep();

        if (worldCoordinateToLookAt != null) {
            var direction = movementComponent.getWorldCoordinate().directionTo(worldCoordinateToLookAt);
            movementComponent.look(direction);
        }
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
