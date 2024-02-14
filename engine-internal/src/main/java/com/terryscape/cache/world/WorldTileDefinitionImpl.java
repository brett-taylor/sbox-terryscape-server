package com.terryscape.cache.world;

public class WorldTileDefinitionImpl implements WorldTileDefinition {

    private boolean walkable;

    @Override
    public boolean isWalkable() {
        return walkable;
    }

    public WorldTileDefinitionImpl setWalkable(boolean walkable) {
        this.walkable = walkable;
        return this;
    }
}
