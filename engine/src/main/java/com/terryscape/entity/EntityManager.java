package com.terryscape.entity;

import com.terryscape.game.npc.NpcComponent;
import com.terryscape.net.Client;

public interface EntityManager {

    void registerEntity(Entity entity);

    void deleteEntity(EntityIdentifier entityIdentifier);

    void sendInitialUpdate(Client client);

    NpcComponent getNpc(EntityIdentifier entityIdentifier);
}
