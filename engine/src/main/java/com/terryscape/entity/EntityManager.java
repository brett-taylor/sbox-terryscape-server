package com.terryscape.entity;

public interface EntityManager {

    Entity createEntity(EntityType entityType);

    void registerEntity(Entity entity);

    void deleteEntity(EntityIdentifier entityIdentifier);

}
