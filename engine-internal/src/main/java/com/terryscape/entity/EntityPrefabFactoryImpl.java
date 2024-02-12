package com.terryscape.entity;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.npc.NpcDefinition;
import com.terryscape.game.chat.PlayerChatComponentImpl;
import com.terryscape.game.chat.command.CommandManager;
import com.terryscape.game.combat.CombatComponentImpl;
import com.terryscape.game.combat.health.HealthComponentImpl;
import com.terryscape.game.combat.script.PlayerCombatScript;
import com.terryscape.game.combat.script.SimpleNpcCombatScript;
import com.terryscape.game.movement.AnimationComponentImpl;
import com.terryscape.game.movement.MovementComponentImpl;
import com.terryscape.game.movement.MovementSpeed;
import com.terryscape.game.npc.NpcComponentImpl;
import com.terryscape.game.player.PlayerComponentImpl;
import com.terryscape.game.player.PlayerGender;
import com.terryscape.game.task.TaskComponentImpl;
import com.terryscape.maths.RandomUtil;
import com.terryscape.net.PacketManager;
import com.terryscape.world.WorldClock;
import com.terryscape.world.WorldManager;
import com.terryscape.world.pathfinding.PathfindingManager;

@Singleton
public class EntityPrefabFactoryImpl implements EntityPrefabFactory {

    private final WorldManager worldManager;

    private final PathfindingManager pathfindingManager;

    private final WorldClock worldClock;

    private final PacketManager packetManager;

    private final CommandManager commandManager;

    @Inject
    public EntityPrefabFactoryImpl(WorldManager worldManager, PathfindingManager pathfindingManager, WorldClock worldClock, PacketManager packetManager,
                                   CommandManager commandManager) {

        this.worldManager = worldManager;
        this.pathfindingManager = pathfindingManager;
        this.worldClock = worldClock;
        this.packetManager = packetManager;
        this.commandManager = commandManager;
    }

    @Override
    public Entity createNpcPrefab(NpcDefinition npcDefinition) {
        var entity = new EntityImpl(EntityIdentifier.randomIdentifier(), EntityPrefabType.NPC, npcDefinition.getId());

        var npcComponent = new NpcComponentImpl(entity, worldManager);
        npcComponent.setNpcDefinition(npcDefinition);

        if (npcDefinition.getSimpleNpc().isPresent()) {
            var variants = npcDefinition.getSimpleNpc().get().getVariants();
            var randomVariant = RandomUtil.randomCollection(variants);
            npcComponent.setNpcVariant(randomVariant);
        }

        entity.addComponent(npcComponent);

        var taskComponent = new TaskComponentImpl(entity);
        entity.addComponent(taskComponent);

        var movementComponent = new MovementComponentImpl(entity, pathfindingManager);
        movementComponent.setMovementSpeed(MovementSpeed.WALK);
        entity.addComponent(movementComponent);

        var healthComponent = new HealthComponentImpl(entity);
        healthComponent.setMaxHealth(5);
        healthComponent.setHealth(5);
        entity.addComponent(healthComponent);

        var animationComponent = new AnimationComponentImpl(entity);
        entity.addComponent(animationComponent);

        var combatScript = new SimpleNpcCombatScript(worldClock, npcComponent);
        var combatComponent = new CombatComponentImpl(entity, pathfindingManager, combatScript);
        entity.addComponent(combatComponent);

        return entity;
    }

    @Override
    public Entity createPlayerPrefab() {
        var entity = new EntityImpl(EntityIdentifier.randomIdentifier(), EntityPrefabType.PLAYER, null);

        var playerComponent = new PlayerComponentImpl(entity, packetManager);
        playerComponent.setGender(PlayerGender.MALE);
        entity.addComponent(playerComponent);

        var playerChatComponent = new PlayerChatComponentImpl(entity, packetManager, commandManager);
        entity.addComponent(playerChatComponent);

        var taskComponent = new TaskComponentImpl(entity);
        entity.addComponent(taskComponent);

        var movementComponent = new MovementComponentImpl(entity, pathfindingManager);
        movementComponent.setMovementSpeed(MovementSpeed.WALK);
        entity.addComponent(movementComponent);

        var healthComponent = new HealthComponentImpl(entity);
        healthComponent.setMaxHealth(10);
        healthComponent.setHealth(10);
        entity.addComponent(healthComponent);

        var animationComponent = new AnimationComponentImpl(entity);
        entity.addComponent(animationComponent);

        var combatScript = new PlayerCombatScript(worldClock, playerComponent);
        var combatComponent = new CombatComponentImpl(entity, pathfindingManager, combatScript);
        entity.addComponent(combatComponent);

        return entity;
    }
}
