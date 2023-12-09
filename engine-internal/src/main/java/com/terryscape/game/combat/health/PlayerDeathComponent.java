package com.terryscape.game.combat.health;

import com.terryscape.entity.Entity;
import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.entity.event.type.OnEntityDeathEntityEvent;
import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.game.combat.health.HealthComponent;
import com.terryscape.game.movement.AnimationComponent;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.task.TaskComponent;
import com.terryscape.game.task.step.impl.ImmediateStep;
import com.terryscape.game.task.step.impl.WaitStep;
import com.terryscape.world.WorldCoordinate;

public class PlayerDeathComponent extends BaseEntityComponent {

    public PlayerDeathComponent(Entity entity) {
        super(entity);

        getEntity().subscribe(OnEntityDeathEntityEvent.class, this::onEntityDeath);
    }

    public void onEntityDeath(OnEntityDeathEntityEvent onEntityDeathEntityEvent) {
        getEntity().getComponentOrThrow(PlayerChatComponent.class).sendGameMessage("You died!");
        getEntity().getComponentOrThrow(AnimationComponent.class).playAnimation("death");
        getEntity().getComponentOrThrow(MovementComponent.class).stop();

        // TODO: Make dying take longer
        getEntity().getComponentOrThrow(TaskComponent.class).setPrimaryTask(
            WaitStep.ticks(5),
            ImmediateStep.run(this::respawn)
        );
    }

    private void respawn() {
        getEntity().getComponentOrThrow(AnimationComponent.class).resetAnimation();
        getEntity().getComponentOrThrow(MovementComponent.class).teleport(new WorldCoordinate(1, 0));

        getEntity().getComponentOrThrow(HealthComponent.class).resetHealthToMax();
    }

}
