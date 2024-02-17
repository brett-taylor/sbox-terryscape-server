package com.terryscape.cache.world;

import com.terryscape.cache.object.ObjectDefinition;
import com.terryscape.world.coordinate.WorldCoordinate;

public interface WorldObjectDefinition {

    ObjectDefinition getObjectDefinition();

    WorldCoordinate getWorldCoordinate();
}
