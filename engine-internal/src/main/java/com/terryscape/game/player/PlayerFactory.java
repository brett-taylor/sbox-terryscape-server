package com.terryscape.game.player;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.CacheLoader;
import com.terryscape.entity.Entity;
import com.terryscape.entity.EntityManager;
import com.terryscape.entity.EntityType;
import com.terryscape.game.chat.PlayerChatComponentImpl;
import com.terryscape.game.movement.PlayerMovementComponentImpl;
import com.terryscape.net.PacketManager;
import com.terryscape.world.pathfinding.PathfindingManager;

@Singleton
public class PlayerFactory {

    private final EntityManager entityManager;

    private final PacketManager packetManager;

    private final PathfindingManager pathfindingManager;

    private final CacheLoader cacheLoader;

    @Inject
    public PlayerFactory(EntityManager entityManager, PacketManager packetManager, PathfindingManager pathfindingManager, CacheLoader cacheLoader) {
        this.entityManager = entityManager;
        this.packetManager = packetManager;
        this.pathfindingManager = pathfindingManager;
        this.cacheLoader = cacheLoader;
    }

    public Entity createUnregisteredEntityWithAllNecessaryPlayerComponents() {
        var entity = entityManager.createEntity(EntityType.PLAYER);

        var playerComponent = new PlayerComponentImpl(entity, packetManager, cacheLoader);
        entity.addComponent(playerComponent);

        var playerChatComponent = new PlayerChatComponentImpl(entity, packetManager);
        entity.addComponent(playerChatComponent);

        var playerMovementComponent = new PlayerMovementComponentImpl(entity, pathfindingManager);
        entity.addComponent(playerMovementComponent);

        return entity;
    }
}
