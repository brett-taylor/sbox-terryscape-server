package com.terryscape.world;

import com.terryscape.entity.Entity;
import com.terryscape.entity.EntityIdentifier;
import com.terryscape.net.Client;

public interface WorldManager {

    void sendInitialUpdate(Client client);

    void registerEntity(Entity entity);

    void deleteEntity(EntityIdentifier entityIdentifier);

    Entity getEntity(EntityIdentifier entityIdentifier);

}
