package com.terryscape.game.player;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.CacheLoader;
import com.terryscape.entity.*;
import com.terryscape.game.chat.PlayerChatComponentImpl;
import com.terryscape.game.combat.health.HealthComponentImpl;
import com.terryscape.game.movement.AnimationComponentImpl;
import com.terryscape.game.movement.MovementComponentImpl;
import com.terryscape.game.npc.NpcDeathComponent;
import com.terryscape.game.task.TaskComponentImpl;
import com.terryscape.net.PacketManager;
import com.terryscape.world.pathfinding.PathfindingManager;

@Singleton
public class PlayerFactory {

    private final PacketManager packetManager;

    private final PathfindingManager pathfindingManager;

    private final CacheLoader cacheLoader;

    @Inject
    public PlayerFactory(PacketManager packetManager, PathfindingManager pathfindingManager, CacheLoader cacheLoader) {
        this.packetManager = packetManager;
        this.pathfindingManager = pathfindingManager;
        this.cacheLoader = cacheLoader;
    }

    public Entity createUnregisteredPlayer() {
        var entity = new EntityImpl(EntityIdentifier.randomIdentifier(), EntityPrefabType.PLAYER, null);

        var playerComponent = new PlayerComponentImpl(entity, packetManager, cacheLoader);
        entity.addComponent(playerComponent);

        var playerChatComponent = new PlayerChatComponentImpl(entity, packetManager);
        entity.addComponent(playerChatComponent);

        var playerMovementComponent = new MovementComponentImpl(entity, pathfindingManager);
        entity.addComponent(playerMovementComponent);

        var healthComponent = new HealthComponentImpl(entity);
        healthComponent.setMaxHealth(10);
        healthComponent.setHealth(1);
        entity.addComponent(healthComponent);

        var playerDeathComponent = new PlayerDeathComponent(entity);
        entity.addComponent(playerDeathComponent);

        var animationComponent = new AnimationComponentImpl(entity);
        entity.addComponent(animationComponent);

        var taskComponent = new TaskComponentImpl(entity);
        entity.addComponent(taskComponent);

        return entity;
    }
}
