package com.terryscape.world;

import com.terryscape.entity.Entity;
import com.terryscape.entity.EntityIdentifier;
import com.terryscape.net.Client;

// TODO: Change back to EntityManager?
public interface WorldManager {

    void sendInitialUpdate(Client client);

    void registerEntity(Entity entity);

    Entity getEntity(EntityIdentifier entityIdentifier);

}
