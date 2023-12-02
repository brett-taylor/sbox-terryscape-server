package com.terryscape.game.task.step.impl;

import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.task.step.Step;
import com.terryscape.world.WorldCoordinate;

public class WalkToStep implements Step {

    public static WalkToStep worldCoordinate(MovementComponent movementComponent, WorldCoordinate destination) {
        return new WalkToStep(movementComponent, destination);
    }

    private final MovementComponent movementComponent;

    private final WorldCoordinate destination;

    private boolean finished;

    private WalkToStep(MovementComponent movementComponent, WorldCoordinate destination) {
        this.movementComponent = movementComponent;
        this.destination = destination;
    }

    @Override
    public void firstTick() {
        var success = movementComponent.move(destination);

        if (!success) {
            finished = true;
        }
    }

    @Override
    public void tick() {
        if (movementComponent.getWorldCoordinate().equals(destination)) {
            finished = true;
        }
    }

    @Override
    public boolean isFinished() {
        return finished;
    }
}
