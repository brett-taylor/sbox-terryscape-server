package com.terryscape.game.combat.script;

import com.terryscape.game.combat.CombatComponent;
import com.terryscape.game.combat.CombatScript;
import com.terryscape.game.combat.health.DamageInformation;
import com.terryscape.game.combat.health.HealthComponent;
import com.terryscape.game.movement.AnimationComponent;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.npc.NpcComponent;
import com.terryscape.world.WorldClock;

public class SimpleNpcCombatScript implements CombatScript {

    private final WorldClock worldClock;

    private final NpcComponent npcComponent;

    private final MovementComponent movementComponent;

    private final AnimationComponent animationComponent;

    private long lastAttackTime;

    public SimpleNpcCombatScript(WorldClock worldClock, NpcComponent npcComponent) {
        this.worldClock = worldClock;
        this.npcComponent = npcComponent;
        this.movementComponent = npcComponent.getEntity().getComponentOrThrow(MovementComponent.class);
        this.animationComponent = npcComponent.getEntity().getComponentOrThrow(AnimationComponent.class);
    }

    @Override
    public boolean isInRange(CombatComponent victim) {
        var victimMovementComponent = victim.getEntity().getComponentOrThrow(MovementComponent.class);
        return movementComponent.getWorldCoordinate().distance(victimMovementComponent.getWorldCoordinate()) <= 2f;
    }

    @Override
    public boolean attack(CombatComponent victim) {
        if (lastAttackTime + 4 > worldClock.getNowTick()) {
            return false;
        }

        lastAttackTime = worldClock.getNowTick();

        var damage = new DamageInformation().setAmount(1).setDamageType(npcComponent.getNpcDefinition().getCombatDamageType());
        victim.getEntity().getComponentOrThrow(HealthComponent.class).takeDamage(damage);

        animationComponent.playAnimation("attack");

        return true;
    }
}
