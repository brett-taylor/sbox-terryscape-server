package com.terryscape.game.movement;

import com.terryscape.world.Direction;
import com.terryscape.world.WorldCoordinate;

public interface PlayerMovement {

    WorldCoordinate getWorldCoordinate();

    Direction getDirection();

    void look(Direction direction);

    void teleport(WorldCoordinate destination);

    void move(WorldCoordinate destination);

    void stop();
}
