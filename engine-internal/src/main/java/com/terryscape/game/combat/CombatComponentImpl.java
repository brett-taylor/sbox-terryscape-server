package com.terryscape.game.combat;

import com.terryscape.cache.CacheLoader;
import com.terryscape.entity.Entity;
import com.terryscape.entity.EntityPrefabType;
import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.game.combat.health.HealthComponent;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.npc.NpcComponent;
import com.terryscape.game.player.PlayerComponent;
import com.terryscape.game.diceroll.CombatDiceRoll;
import com.terryscape.game.task.Task;
import com.terryscape.game.task.TaskComponent;
import com.terryscape.world.pathfinding.PathfindingManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CombatComponentImpl extends BaseEntityComponent implements CombatComponent {

    private static final Logger LOGGER = LogManager.getLogger(CombatComponentImpl.class);

    private final PathfindingManager pathfindingManager;

    private final CacheLoader cacheLoader;

    private final CombatScript combatScript;

    private final CombatDiceRoll combatDiceRoll;

    private TaskComponent taskComponent;

    private CombatComponent victim;

    private Task combatTask;

    private int cooldown;

    public CombatComponentImpl(Entity entity,
                               PathfindingManager pathfindingManager,
                               CacheLoader cacheLoader,
                               CombatScript combatScript,
                               CombatDiceRoll combatDiceRoll) {
        super(entity);

        this.pathfindingManager = pathfindingManager;
        this.cacheLoader = cacheLoader;
        this.combatScript = combatScript;
        this.combatDiceRoll = combatDiceRoll;
    }

    @Override
    public void onRegistered() {
        super.onRegistered();

        taskComponent = getEntity().getComponentOrThrow(TaskComponent.class);
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
        var selfMovement = getEntity().getComponentOrThrow(MovementComponent.class);
        var victimMovement = victim.getEntity().getComponentOrThrow(MovementComponent.class);

        var task = taskComponent.setCancellablePrimaryTask(new CombatFollowTaskStep(pathfindingManager, cacheLoader, selfMovement, victimMovement));
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
    public void stopAttacking() {
        if (combatTask != null) {
            combatTask.cancel();
            victim = null;
            combatTask = null;
        }
    }

    @Override
    public void attackedBy(CombatComponent attacker) {
        getEntity().invoke(OnAttackedEntityEvent.class, new OnAttackedEntityEvent(attacker));
    }

    @Override
    public void tick() {
        super.tick();

        if (cooldown > 0) {
            cooldown -= 1;
        }

        if (cooldown > 0) {
            return;
        }

        if (victim == null || combatTask == null) {
            return;
        }

        if (!victim.getEntity().isValid()) {
            LOGGER.warn("Attacker {} stopped attacking because victim {} is no longer valid.", getEntity().getIdentifier(), victim.getEntity().getIdentifier());
            stopAttacking();
            return;
        }

        if (!combatScript.isInRange(victim)) {
            return;
        }

        var didAttack = combatScript.attack(this, victim, combatDiceRoll);
        if (!didAttack) {
            return;
        }

        victim.attackedBy(this);

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
