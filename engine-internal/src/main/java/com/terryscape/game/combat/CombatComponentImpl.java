package com.terryscape.game.combat;

import com.terryscape.entity.Entity;
import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.game.combat.health.HealthComponent;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.npc.NpcComponent;
import com.terryscape.game.player.PlayerComponent;
import com.terryscape.game.task.Task;
import com.terryscape.game.task.TaskComponent;
import com.terryscape.game.task.step.impl.ImmediateStep;
import com.terryscape.game.task.step.impl.WaitStep;
import com.terryscape.world.pathfinding.PathfindingManager;

public class CombatComponentImpl extends BaseEntityComponent implements CombatComponent {

    private final PathfindingManager pathfindingManager;

    private final CombatScript combatScript;

    private TaskComponent taskComponent;

    private CombatComponent victim;

    private Task combatTask;

    public CombatComponentImpl(Entity entity, PathfindingManager pathfindingManager, CombatScript combatScript) {
        super(entity);

        this.pathfindingManager = pathfindingManager;
        this.combatScript = combatScript;
    }


    @Override
    public void onRegistered() {
        super.onRegistered();

        taskComponent = getEntity().getComponentOrThrow(TaskComponent.class);
    }

    @Override
    public void attack(CombatComponent victim) {
        var selfMovement = getEntity().getComponentOrThrow(MovementComponent.class);
        var victimMovement = victim.getEntity().getComponentOrThrow(MovementComponent.class);

        var task = taskComponent.setCancellablePrimaryTask(new CombatFollowStep(pathfindingManager, selfMovement, victimMovement));
        if (task.isPresent()) {
            this.victim = victim;

            combatTask = task.get();
            combatTask.onFinished(taskFinishedReason -> {
                this.victim = null;
                this.combatTask = null;
            });
        }
    }

    @Override
    public void attackedBy(CombatComponent attacker) {
        attack(attacker);
    }

    @Override
    public void tick() {
        super.tick();

        if (victim == null || combatTask == null) {
            return;
        }

        if (!combatScript.isInRange(victim)) {
            return;
        }

        var didAttack = combatScript.attack(victim);
        if (!didAttack) {
            return;
        }

        victim.attackedBy(this);

        if (victim.getEntity().getComponentOrThrow(HealthComponent.class).isDying()) {
            // TODO: Swap this to fire like a killed event or something that we can live to in the PlayerComponent
            var playerChat = getEntity().getComponent(PlayerChatComponent.class);
            var victimNpc = victim.getEntity().getComponent(NpcComponent.class);
            var victimPlayer = victim.getEntity().getComponent(PlayerComponent.class);

            if (playerChat.isPresent() && victimNpc.isPresent()) {
                playerChat.get().sendGameMessage("You killed npc %s".formatted(victimNpc.get().getNpcDefinition().getName()));
            }

            if (playerChat.isPresent() && victimPlayer.isPresent()) {
                playerChat.get().sendGameMessage("You killed player %s".formatted(victimPlayer.get().getUsername()));
            }

            // TODO: Swap this to like subscribing to the entity's death event or something
            combatTask.cancel();
            victim = null;
            combatTask = null;
        }
    }

}
