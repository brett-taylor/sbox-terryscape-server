package com.terryscape.game.combat;

import com.terryscape.entity.Entity;
import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.game.combat.health.HealthComponent;
import com.terryscape.game.diceroll.CombatDiceRoll;
import com.terryscape.game.npc.NpcComponent;
import com.terryscape.game.player.PlayerComponent;
import com.terryscape.game.task.Task;
import com.terryscape.game.task.TaskComponent;
import com.terryscape.world.pathfinding.PathfindingManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CombatComponentImpl extends BaseEntityComponent implements CombatComponent {

    private static final Logger LOGGER = LogManager.getLogger(CombatComponentImpl.class);

    private final PathfindingManager pathfindingManager;

    private final CombatDiceRoll combatDiceRoll;

    private CombatScript combatScript;

    private TaskComponent taskComponent;

    private CombatComponent victim;

    private Task combatTask;

    private int cooldown;

    private CombatFollow combatFollow;

    public CombatComponentImpl(Entity entity,
                               PathfindingManager pathfindingManager,
                               CombatDiceRoll combatDiceRoll) {
        super(entity);

        this.pathfindingManager = pathfindingManager;
        this.combatDiceRoll = combatDiceRoll;
    }

    @Override
    public void onRegistered() {
        super.onRegistered();

        taskComponent = getEntity().getComponentOrThrow(TaskComponent.class);
    }

    public void setCombatScript(CombatScript combatScript) {
        this.combatScript = combatScript;
    }

    public CombatScript getCombatScript() {
        return combatScript;
    }

    @Override
    public boolean isInCombat() {
        return combatTask != null;
    }

    @Override
    public void ensureCooldownOfAtLeast(int ticks) {
        if (cooldown > ticks) {
            return;
        }

        cooldown = ticks;
    }

    @Override
    public void attack(CombatComponent victim) {
        var task = taskComponent.setCancellablePrimaryTask(new CombatTaskStep());

        if (task.isEmpty()) {
            return;
        }

        this.victim = victim;
        combatTask = task.get();
        combatTask.onFinished(taskFinishedReason -> {
            if (combatFollow != null) {
                combatFollow.stop();
                combatTask = null;
            }

            stopAttacking();
        });

        combatFollow = new CombatFollow(pathfindingManager, this, (CombatComponentImpl) victim);
    }

    @Override
    public void stopAttacking() {
        if (combatTask != null) {
            combatTask.cancel();
            combatTask = null;
        }

        if (combatFollow != null) {
            combatFollow.stop();
            combatFollow = null;
        }

        victim = null;
    }

    @Override
    public void tick() {
        super.tick();

        if (cooldown > 0) {
            cooldown -= 1;
        }

        if (combatTask == null) {
            return;
        }

        if (!victim.getEntity().isValid()) {
            LOGGER.warn("Attacker {} stopped attacking because victim {} is no longer valid.", getEntity().getIdentifier(), victim.getEntity().getIdentifier());
            stopAttacking();
            return;
        }

        var combatFollowResult = combatFollow.tick();
        if (combatFollowResult.shouldStopAttacking()) {
            stopAttacking();
        } else if (combatFollowResult.canAttack()) {
            tickAttack();
        }
    }

    private void tickAttack() {
        if (cooldown > 0) {
            return;
        }

        var didAttack = combatScript.attack(this, victim, combatDiceRoll);
        if (!didAttack) {
            return;
        }

        getEntity().invoke(OnAttackEntityEvent.class, new OnAttackEntityEvent(victim));
        victim.getEntity().invoke(OnAttackedEntityEvent.class, new OnAttackedEntityEvent(this));

        // TODO: Swap this to like subscribing to the entity's death event or something
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

            stopAttacking();
        }
    }
}
