package com.terryscape.game.movement;

import com.terryscape.entity.Entity;
import com.terryscape.entity.component.NetworkedEntityComponent;
import com.terryscape.world.Direction;
import com.terryscape.world.WorldCoordinate;
import com.terryscape.world.pathfinding.PathfindingRoute;

public interface MovementComponent extends NetworkedEntityComponent {

    WorldCoordinate getWorldCoordinate();

    Direction getDirection();

    void look(Direction direction);

    void face(MovementComponent movementComponent);

    void teleport(WorldCoordinate destination);

    boolean move(WorldCoordinate destination);

    void stop();

    void stopFacing();
}
