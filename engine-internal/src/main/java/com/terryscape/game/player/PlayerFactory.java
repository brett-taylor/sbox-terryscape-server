package com.terryscape.game.player;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.CacheLoader;
import com.terryscape.entity.Entity;
import com.terryscape.entity.EntityIdentifier;
import com.terryscape.entity.EntityImpl;
import com.terryscape.entity.EntityPrefabType;
import com.terryscape.game.chat.PlayerChatComponentImpl;
import com.terryscape.game.chat.command.CommandManager;
import com.terryscape.game.combat.CombatComponentImpl;
import com.terryscape.game.combat.health.HealthComponentImpl;
import com.terryscape.game.combat.script.PlayerCombatScript;
import com.terryscape.game.movement.AnimationComponentImpl;
import com.terryscape.game.movement.MovementComponentImpl;
import com.terryscape.game.movement.MovementSpeed;
import com.terryscape.game.task.TaskComponentImpl;
import com.terryscape.net.PacketManager;
import com.terryscape.world.WorldClock;
import com.terryscape.world.pathfinding.PathfindingManager;

@Singleton
public class PlayerFactory {

    private final PacketManager packetManager;

    private final PathfindingManager pathfindingManager;

    private final WorldClock worldClock;

    private final CommandManager commandManager;

    @Inject
    public PlayerFactory(PacketManager packetManager, PathfindingManager pathfindingManager, CacheLoader cacheLoader, WorldClock worldClock,
                         CommandManager commandManager) {

        this.packetManager = packetManager;
        this.pathfindingManager = pathfindingManager;
        this.worldClock = worldClock;
        this.commandManager = commandManager;
    }

    public Entity createUnregisteredPlayer() {
        var entity = new EntityImpl(EntityIdentifier.randomIdentifier(), EntityPrefabType.PLAYER, null);

        var healthComponent = new HealthComponentImpl(entity);
        healthComponent.setMaxHealth(10);
        healthComponent.setHealth(10);
        entity.addComponent(healthComponent);

        var playerComponent = new PlayerComponentImpl(entity, packetManager);
        playerComponent.setGender(PlayerGender.MALE);
        entity.addComponent(playerComponent);

        var playerChatComponent = new PlayerChatComponentImpl(entity, packetManager, commandManager);
        entity.addComponent(playerChatComponent);

        var taskComponent = new TaskComponentImpl(entity);
        entity.addComponent(taskComponent);

        var movementComponent = new MovementComponentImpl(entity, pathfindingManager);
        movementComponent.setMovementSpeed(MovementSpeed.RUN);
        entity.addComponent(movementComponent);


        var animationComponent = new AnimationComponentImpl(entity);
        entity.addComponent(animationComponent);

        var combatScript = new PlayerCombatScript(worldClock, playerComponent);
        var combatComponent = new CombatComponentImpl(entity, pathfindingManager, combatScript);
        entity.addComponent(combatComponent);

        return entity;
    }
}
