package com.terryscape.entity;

import com.terryscape.entity.player.Player;

public interface EntityManager {

    void registerEntity(Entity entity);

    void unregisterEntity(Entity entity);

    void sendPlayerInitialUpdate(Player player);
}
