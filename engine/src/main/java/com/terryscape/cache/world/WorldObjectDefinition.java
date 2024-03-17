package com.terryscape.cache.world;

import com.terryscape.cache.object.ObjectDefinition;
import com.terryscape.game.world.coordinate.WorldCoordinate;

public interface WorldObjectDefinition {

    ObjectDefinition getObjectDefinition();

    WorldCoordinate getWorldCoordinate();
}
