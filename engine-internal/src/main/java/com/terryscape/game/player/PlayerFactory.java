package com.terryscape.game.player;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.CacheLoader;
import com.terryscape.entity.*;
import com.terryscape.game.chat.PlayerChatComponentImpl;
import com.terryscape.game.movement.MovementComponentImpl;
import com.terryscape.net.PacketManager;
import com.terryscape.world.pathfinding.PathfindingManager;

@Singleton
public class PlayerFactory {

    private final PacketManager packetManager;

    private final PathfindingManager pathfindingManager;

    private final CacheLoader cacheLoader;

    @Inject
    public PlayerFactory(EntityManager entityManager, PacketManager packetManager, PathfindingManager pathfindingManager, CacheLoader cacheLoader) {
        this.packetManager = packetManager;
        this.pathfindingManager = pathfindingManager;
        this.cacheLoader = cacheLoader;
    }

    public Entity createUnregisteredPlayer() {
        var entity = new EntityImpl(EntityIdentifier.randomIdentifier(), EntityPrefabType.PLAYER, "");

        var playerComponent = new PlayerComponentImpl(entity, packetManager, cacheLoader);
        entity.addComponent(playerComponent);

        var playerChatComponent = new PlayerChatComponentImpl(entity, packetManager);
        entity.addComponent(playerChatComponent);

        var playerMovementComponent = new MovementComponentImpl(entity, pathfindingManager);
        entity.addComponent(playerMovementComponent);

        return entity;
    }
}
