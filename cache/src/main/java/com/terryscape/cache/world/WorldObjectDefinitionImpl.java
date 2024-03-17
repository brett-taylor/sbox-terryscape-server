package com.terryscape.cache.world;

import com.terryscape.cache.object.ObjectDefinition;
import com.terryscape.cache.object.ObjectDefinitionImpl;
import com.terryscape.world.coordinate.WorldCoordinate;

public class WorldObjectDefinitionImpl implements WorldObjectDefinition {

    private ObjectDefinitionImpl objectDefinition;

    private WorldCoordinate worldCoordinate;

    @Override
    public ObjectDefinition getObjectDefinition() {
        return objectDefinition;
    }

    public WorldObjectDefinitionImpl setObjectDefinition(ObjectDefinitionImpl objectDefinition) {
        this.objectDefinition = objectDefinition;
        return this;
    }

    @Override
    public WorldCoordinate getWorldCoordinate() {
        return worldCoordinate;
    }

    public WorldObjectDefinitionImpl setWorldCoordinate(WorldCoordinate worldCoordinate) {
        this.worldCoordinate = worldCoordinate;
        return this;
    }
}
