package com.terryscape.world;

import com.terryscape.entity.Entity;
import com.terryscape.entity.EntityIdentifier;
import com.terryscape.game.player.PlayerComponent;
import com.terryscape.net.Client;

import java.util.Set;

public interface WorldManager {

    void sendInitialUpdate(Client client);

    void registerEntity(Entity entity);

    Entity getEntity(EntityIdentifier entityIdentifier);

    Set<PlayerComponent> getPlayers();

}
