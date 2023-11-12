package com.terryscape.entity;

public interface EntityManager {

    Entity createEntity(EntityPrefabType entityPrefabType);

    void registerEntity(Entity entity);

    void deleteEntity(EntityIdentifier entityIdentifier);

}
