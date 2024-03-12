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

import java.util.ArrayList;
import java.util.List;

public class CombatComponentImpl extends BaseEntityComponent implements CombatComponent {

    private static final Logger LOGGER = LogManager.getLogger(CombatComponentImpl.class);

    private final PathfindingManager pathfindingManager;

    private final CombatDiceRoll combatDiceRoll;

    private final List<PendingCombatHit> pendingCombatHits;

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
        this.pendingCombatHits = new ArrayList<>();
    }

    @Override
    public void onRegistered() {
        super.onRegistered();

        taskComponent = getEntity().getComponentOrThrow(TaskComponent.class);
    }

    @Override
    public void setCombatScript(CombatScript combatScript) {
        this.combatScript = combatScript;
        this.combatScript.setOwner(this);
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
    public void registerAttack(CombatComponent victim, CombatHit combatHit) {
        var pendingHit = new PendingCombatHit(this, victim, combatHit);

        pendingHit.getCombatHit().onRegistered(this, victim);

        if (pendingHit.shouldExecute()) {
            executePendingCombatHit(pendingHit);
            return;
        }

        pendingCombatHits.add(pendingHit);
    }

    @Override
    public void tick() {
        super.tick();

        if (cooldown > 0) {
            cooldown -= 1;
        }

        tickPendingCombatHits();

        var shouldContinue = checkVictimStateAndContinueAttacking();
        if (!shouldContinue) {
            return;
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
            return;
        }

        if (!combatFollowResult.canAttack() || cooldown > 0) {
            return;
        }

        combatScript.attack(victim);
        checkVictimStateAndContinueAttacking();
    }

    private void tickPendingCombatHits() {
        pendingCombatHits.forEach(PendingCombatHit::tick);
        var pendingHitsToExecute = pendingCombatHits.stream().filter(PendingCombatHit::shouldExecute).toList();
        pendingHitsToExecute.forEach(this::executePendingCombatHit);
        pendingCombatHits.removeAll(pendingHitsToExecute);
    }

    private void executePendingCombatHit(PendingCombatHit pendingCombatHit){
        pendingCombatHit.getCombatHit().executeHit(this, pendingCombatHit.getVictim(), combatDiceRoll);

        getEntity().invoke(OnAttackEntityEvent.class, new OnAttackEntityEvent(pendingCombatHit.getVictim()));
        pendingCombatHit.getVictim().getEntity().invoke(OnAttackedEntityEvent.class, new OnAttackedEntityEvent(this));
    }

    private boolean checkVictimStateAndContinueAttacking() {
        if (this.victim == null) {
            return false;
        }

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
            return false;
        }

        return true;
    }

}
