package com.terryscape.game.movement;

import com.terryscape.entity.component.NetworkedEntityComponent;
import com.terryscape.world.Direction;
import com.terryscape.world.WorldCoordinate;

public interface MovementComponent extends NetworkedEntityComponent {

    WorldCoordinate getWorldCoordinate();

    Direction getDirection();

    void setMovementSpeed(MovementSpeed movementSpeed);

    void look(Direction direction);

    void face(MovementComponent movementComponent);

    void teleport(WorldCoordinate destination);

    boolean move(WorldCoordinate destination);

    void stop();

    void stopFacing();
}
