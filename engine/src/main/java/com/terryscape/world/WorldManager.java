package com.terryscape.world;

import com.terryscape.entity.Entity;
import com.terryscape.entity.EntityIdentifier;

public interface WorldManager {

    void registerEntity(Entity entity);

    void registerEntity(Entity entity, WorldRegion worldRegion);

    void deleteEntity(EntityIdentifier entityIdentifier);

    Entity getEntity(EntityIdentifier entityIdentifier);

    void registerEntityToWorldRegion(EntityIdentifier entityIdentifier, WorldRegion worldRegion);

    WorldRegion getWorldRegionFromWorldCoordinate(WorldCoordinate worldCoordinate);

}
