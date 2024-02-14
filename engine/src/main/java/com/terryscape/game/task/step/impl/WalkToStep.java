package com.terryscape.game.task.step.impl;

import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.task.step.Step;
import com.terryscape.world.coordinate.WorldCoordinate;

public class WalkToStep extends Step {

    public static WalkToStep worldCoordinate(MovementComponent movementComponent, WorldCoordinate destination) {
        return new WalkToStep(movementComponent, destination);
    }

    private final MovementComponent movementComponent;

    private final WorldCoordinate destination;

    private WalkToStep(MovementComponent movementComponent, WorldCoordinate destination) {
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
