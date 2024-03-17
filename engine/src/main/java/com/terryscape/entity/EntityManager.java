package com.terryscape.entity;

import com.terryscape.game.player.PlayerComponent;
import com.terryscape.net.Client;

import java.util.Set;

public interface EntityManager {

    void sendInitialUpdate(Client client);

    void registerEntity(Entity entity);

    Entity getEntity(EntityIdentifier entityIdentifier);

    Set<PlayerComponent> getPlayers();

}
