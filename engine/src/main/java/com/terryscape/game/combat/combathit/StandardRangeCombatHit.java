package com.terryscape.game.combat.combathit;

import com.terryscape.game.combat.CombatComponent;
import com.terryscape.game.combat.DamageType;
import com.terryscape.game.diceroll.CombatDiceRoll;
import com.terryscape.game.movement.AnimationComponent;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.projectile.ProjectileFactory;

// TODO Probably should move into a content module?
public class StandardRangeCombatHit extends StandardCombatFormulaHit {

    private final DamageType damageType;

    private final String attackAnimationId;

    public StandardRangeCombatHit(DamageType damageType, String attackAnimationId) {
        this.damageType = damageType;
        this.attackAnimationId = attackAnimationId;
    }

    @Override
    public void onRegistered(CombatComponent attacker, CombatComponent victim, ProjectileFactory projectileFactory) {
        attacker.getEntity().getComponentOrThrow(AnimationComponent.class).playAnimation(attackAnimationId);

        // TODO Better way of creating projectiles, this shouldn't be needed.
        projectileFactory.createRegisteredProjectile("basic_arrow", projectileComponent -> {
            projectileComponent.setEntitySource(attacker.getEntity());
            projectileComponent.setEntityTarget(victim.getEntity());
            projectileComponent.setLifeSpan(calculateHitDelayTicks(attacker, victim));
        });
    }

    @Override
    public int calculateHitDelayTicks(CombatComponent attacker, CombatComponent victim) {
        var attackerWorldCoordinate = attacker.getEntity().getComponentOrThrow(MovementComponent.class).getWorldCoordinate();
        var victimWorldCoordinate = victim.getEntity().getComponentOrThrow(MovementComponent.class).getWorldCoordinate();

        var possibleTime = Math.round(attackerWorldCoordinate.tileDistance(victimWorldCoordinate) / 4f);
        return Math.max(1, possibleTime);
    }

    @Override
    public void executeHit(CombatComponent attacker, CombatComponent victim, CombatDiceRoll combatDiceRoll) {
        super.executeHit(attacker, victim, combatDiceRoll);
    }

    @Override
    protected DamageType getDamageType() {
        return damageType;
    }
}
