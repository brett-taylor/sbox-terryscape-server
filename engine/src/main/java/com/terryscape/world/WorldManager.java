package com.terryscape.world;

import com.terryscape.entity.Entity;
import com.terryscape.entity.EntityIdentifier;
import com.terryscape.net.Client;
import com.terryscape.world.coordinate.WorldCoordinate;

import java.util.Optional;

// TODO: Change back to EntityManager?
public interface WorldManager {

    void sendInitialUpdate(Client client);

    void registerEntity(Entity entity);

    void deleteEntity(EntityIdentifier entityIdentifier);

    Entity getEntity(EntityIdentifier entityIdentifier);

    Optional<Entity> getEntity(WorldCoordinate coordinate);
}
