package com.terryscape.game.npc;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.npc.NpcDefinition;
import com.terryscape.entity.*;
import com.terryscape.game.combat.health.HealthComponentImpl;
import com.terryscape.game.movement.AnimationComponentImpl;
import com.terryscape.game.movement.MovementComponentImpl;
import com.terryscape.game.task.TaskComponentImpl;
import com.terryscape.util.RandomUtil;
import com.terryscape.world.pathfinding.PathfindingManager;

@Singleton
public class NpcFactoryImpl implements NpcFactory {

    private final EntityManager entityManager;

    private final PathfindingManager pathfindingManager;

    @Inject
    public NpcFactoryImpl(EntityManager entityManager, PathfindingManager pathfindingManager) {
        this.entityManager = entityManager;
        this.pathfindingManager = pathfindingManager;
    }

    @Override
    public Entity createUnregisteredNpc(NpcDefinition npcDefinition) {
        var entity = new EntityImpl(EntityIdentifier.randomIdentifier(), EntityPrefabType.NPC, npcDefinition.getId());

        var npcComponent = new NpcComponentImpl(entity);
        npcComponent.setNpcDefinition(npcDefinition);

        if (npcDefinition.getSimpleNpc().isPresent()) {
            var variants = npcDefinition.getSimpleNpc().get().getVariants();
            var randomVariant = RandomUtil.randomCollection(variants);
            npcComponent.setNpcVariant(randomVariant);
        }

        entity.addComponent(npcComponent);

        var movementComponent = new MovementComponentImpl(entity, pathfindingManager);
        entity.addComponent(movementComponent);

        var healthComponent = new HealthComponentImpl(entity);
        healthComponent.setMaxHealth(10);
        healthComponent.setHealth(10);
        entity.addComponent(healthComponent);

        var npcDeathComponent = new NpcDeathComponent(entity, entityManager);
        entity.addComponent(npcDeathComponent);

        var animationComponent = new AnimationComponentImpl(entity);
        entity.addComponent(animationComponent);

        var taskComponent = new TaskComponentImpl(entity);
        entity.addComponent(taskComponent);

        return entity;
    }

}
