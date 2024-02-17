package com.terryscape.cache.world;

import com.terryscape.cache.object.ObjectDefinition;
import com.terryscape.cache.object.ObjectDefinitionImpl;

public class WorldObjectDefinitionImpl implements WorldObjectDefinition {

    private ObjectDefinitionImpl objectDefinition;

    @Override
    public ObjectDefinition getObjectDefinition() {
        return objectDefinition;
    }

    public WorldObjectDefinitionImpl setObjectDefinition(ObjectDefinitionImpl objectDefinition) {
        this.objectDefinition = objectDefinition;
        return this;
    }
}
