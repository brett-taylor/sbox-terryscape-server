package com.terryscape.world;

import com.google.common.collect.Sets;
import com.terryscape.entity.EntityIdentifier;
import com.terryscape.entity.EntityImpl;
import com.terryscape.entity.EntityPrefabType;

import java.util.*;

public class WorldRegionImpl implements WorldRegion {

    private final WorldRegionCoordinate worldRegionCoordinate;

    private final Set<EntityIdentifier> entities = new HashSet<>();

    private final Set<EntityIdentifier> entitiesAdded = new HashSet<>();

    private final Set<EntityIdentifier> entitiesRemoved = new HashSet<>();

    public WorldRegionImpl(WorldRegionCoordinate worldRegionCoordinate) {
        this.worldRegionCoordinate = worldRegionCoordinate;
    }

    public WorldRegionCoordinate getWorldRegionCoordinate() {
        return worldRegionCoordinate;
    }

    public void addEntity(EntityIdentifier entityIdentifier) {
        entitiesAdded.add(entityIdentifier);
        entities.add(entityIdentifier);
    }

    public void removeEntity(EntityIdentifier entityIdentifier) {
        entitiesRemoved.add(entityIdentifier);
        entities.remove(entityIdentifier);
    }

    public Set<EntityIdentifier> getEntitiesInRegion() {
        return Collections.unmodifiableSet(entities);
    }

    public Set<EntityIdentifier> getEntitiesInRegionOrRemoved() {
        return Sets.union(entities, entitiesRemoved);
    }

    public Set<EntityIdentifier> getEntitiesAdded() {
        return Collections.unmodifiableSet(entitiesAdded);
    }

    public Set<EntityIdentifier> getEntitiesRemoved() {
        return Collections.unmodifiableSet(entitiesRemoved);
    }

    public void clearEntityTracking() {
        entitiesAdded.clear();
        entitiesRemoved.clear();
    }

    @Override
    public String toString() {
        return "WorldRegionImpl(coordinate=%s)".formatted(getWorldRegionCoordinate());
    }
}
