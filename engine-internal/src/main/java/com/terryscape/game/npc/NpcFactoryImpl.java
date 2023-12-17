package com.terryscape.game.npc;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.npc.NpcDefinition;
import com.terryscape.entity.*;
import com.terryscape.game.combat.CombatComponentImpl;
import com.terryscape.game.combat.health.HealthComponentImpl;
import com.terryscape.game.combat.script.SimpleNpcCombatScript;
import com.terryscape.game.movement.AnimationComponentImpl;
import com.terryscape.game.movement.MovementComponentImpl;
import com.terryscape.game.movement.MovementSpeed;
import com.terryscape.game.task.TaskComponentImpl;
import com.terryscape.maths.RandomUtil;
import com.terryscape.world.WorldClock;
import com.terryscape.world.pathfinding.PathfindingManager;

@Singleton
public class NpcFactoryImpl implements NpcFactory {

    private final EntityManager entityManager;

    private final PathfindingManager pathfindingManager;

    private final WorldClock worldClock;

    @Inject
    public NpcFactoryImpl(EntityManager entityManager, PathfindingManager pathfindingManager, WorldClock worldClock) {
        this.entityManager = entityManager;
        this.pathfindingManager = pathfindingManager;
        this.worldClock = worldClock;
    }

    @Override
    public Entity createUnregisteredNpc(NpcDefinition npcDefinition) {
        var entity = new EntityImpl(EntityIdentifier.randomIdentifier(), EntityPrefabType.NPC, npcDefinition.getId());

        var npcComponent = new NpcComponentImpl(entity, entityManager);
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

}
