package com.terryscape.game.movement;

import com.terryscape.entity.component.NetworkedEntityComponent;
import com.terryscape.world.Direction;
import com.terryscape.world.WorldCoordinate;

public interface MovementComponent extends NetworkedEntityComponent {

    WorldCoordinate getWorldCoordinate();

    Direction getDirection();

    void look(Direction direction);

    void teleport(WorldCoordinate destination);

    void move(WorldCoordinate destination);

    void stop();
}
