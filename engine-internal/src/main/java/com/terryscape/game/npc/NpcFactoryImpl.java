package com.terryscape.game.npc;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.NpcDefinition;
import com.terryscape.entity.Entity;
import com.terryscape.entity.EntityIdentifier;
import com.terryscape.entity.EntityImpl;
import com.terryscape.entity.EntityPrefabType;
import com.terryscape.game.combat.health.HealthComponentImpl;
import com.terryscape.game.movement.MovementComponentImpl;
import com.terryscape.world.pathfinding.PathfindingManager;

@Singleton
public class NpcFactoryImpl implements NpcFactory {

    private final PathfindingManager pathfindingManager;

    @Inject
    public NpcFactoryImpl(PathfindingManager pathfindingManager) {
        this.pathfindingManager = pathfindingManager;
    }

    @Override
    public Entity createUnregisteredNpc(NpcDefinition npcDefinition) {
        var entity = new EntityImpl(EntityIdentifier.randomIdentifier(), EntityPrefabType.NPC, npcDefinition.getId());

        var npcComponent = new NpcComponentImpl(entity);
        npcComponent.setNpcDefinition(npcDefinition);
        entity.addComponent(npcComponent);

        var movementComponent = new MovementComponentImpl(entity, pathfindingManager);
        entity.addComponent(movementComponent);

        var healthComponent = new HealthComponentImpl(entity);
        healthComponent.setMaxHealth(250);
        healthComponent.setHealth(250);
        entity.addComponent(healthComponent);

        return entity;
    }

}
