package com.terryscape.game.task.step.impl;

import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.task.step.TaskStep;
import com.terryscape.world.coordinate.WorldCoordinate;

public class WalkToTaskStep extends TaskStep {

    public static WalkToTaskStep worldCoordinate(MovementComponent movementComponent, WorldCoordinate destination) {
        return new WalkToTaskStep(movementComponent, destination);
    }

    private final MovementComponent movementComponent;

    private final WorldCoordinate destination;

    private WalkToTaskStep(MovementComponent movementComponent, WorldCoordinate destination) {
        this.movementComponent = movementComponent;
        this.destination = destination;
    }

    @Override
    public void firstTick() {
        var success = movementComponent.move(destination);

        if (!success) {
            failed();
        }
    }

    @Override
    public boolean isFinished() {
        return movementComponent.getWorldCoordinate().equals(destination);
    }
}
