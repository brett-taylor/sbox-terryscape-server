package com.terryscape.game.npc;

import com.terryscape.entity.Entity;
import com.terryscape.entity.EntityManager;
import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.entity.event.type.OnEntityDeathEntityEvent;
import com.terryscape.game.movement.AnimationComponent;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.task.TaskComponent;
import com.terryscape.game.task.step.impl.ImmediateStep;
import com.terryscape.game.task.step.impl.WaitStep;

public class NpcDeathComponent extends BaseEntityComponent {

    private final EntityManager entityManager;

    public NpcDeathComponent(Entity entity, EntityManager entityManager) {
        super(entity);
        this.entityManager = entityManager;

        getEntity().subscribe(OnEntityDeathEntityEvent.class, this::onEntityDeath);
    }

    private void onEntityDeath(OnEntityDeathEntityEvent onEntityDeathEntityEvent) {
        getEntity().getComponentOrThrow(AnimationComponent.class).playAnimation("death");

        getEntity().getComponentOrThrow(MovementComponent.class).stop();
        getEntity().getComponent(WanderMovementComponent.class).ifPresent(WanderMovementComponent::stopWander);

        getEntity().getComponentOrThrow(TaskComponent.class).setPrimaryTask(
            WaitStep.ticks(5),
            ImmediateStep.run(() -> entityManager.deleteEntity(getEntity().getIdentifier()))
        );
    }
}
