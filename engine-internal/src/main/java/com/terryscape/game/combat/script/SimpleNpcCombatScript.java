package com.terryscape.game.combat.script;

import com.terryscape.game.combat.CharacterStatsImpl;
import com.terryscape.game.combat.CombatComponent;
import com.terryscape.game.combat.CombatScript;
import com.terryscape.game.combat.health.DamageInformation;
import com.terryscape.game.combat.health.DamageType;
import com.terryscape.game.combat.health.HealthComponent;
import com.terryscape.game.movement.AnimationComponent;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.npc.NpcComponent;
import com.terryscape.world.WorldClock;

import java.util.Random;

public class SimpleNpcCombatScript implements CombatScript {
    private final DamageType damageType;
    private final WorldClock worldClock;

    private final NpcComponent npcComponent;

    private final MovementComponent movementComponent;

    private final AnimationComponent animationComponent;

    private long lastAttackTime;

    public SimpleNpcCombatScript(WorldClock worldClock, NpcComponent npcComponent, DamageType damageType) {
        this.worldClock = worldClock;
        this.npcComponent = npcComponent;
        this.damageType = damageType;
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

        animationComponent.playAnimation("attack");
        var victimStats = victim.getEntity().getComponentOrThrow(CharacterStatsImpl.class);
        var attackerStats = npcComponent.getEntity().getComponentOrThrow(CharacterStatsImpl.class);

        var weaponDamageType = damageType;
        double victimEvasion = victimStats.GetEvasion(weaponDamageType);
        double attackerAccuracy = attackerStats.GetAccuracy(weaponDamageType);

        var rand = new Random();
        double hitChance = 1;
        if(victimEvasion > 0) {
            hitChance = attackerAccuracy / victimEvasion;
        }
        var hitAttempt = rand.nextDouble();
        var hit = hitChance < hitAttempt;

        var damageAmount = attackerStats.GetProficiency(weaponDamageType);
        var randomDouble = rand.nextDouble();
        var positiveBias = Math.pow(randomDouble, 0.4);

        damageAmount = (int) (Math.ceil(damageAmount * positiveBias) + 0.05f);

        var damage = new DamageInformation()
                .setHit(hit)
                .setIsUsingMainHand(true)
                .setType(weaponDamageType)
                .setAmount(damageAmount);

        if(hit) {
            victim.getEntity().getComponentOrThrow(HealthComponent.class).takeDamage(damage);
        }
        return true;
    }
}
