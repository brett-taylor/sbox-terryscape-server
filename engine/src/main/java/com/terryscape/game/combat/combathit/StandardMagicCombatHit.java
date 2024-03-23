package com.terryscape.game.combat.combathit;

import com.terryscape.game.combat.CombatComponent;
import com.terryscape.game.combat.DamageType;
import com.terryscape.game.diceroll.CombatDiceRoll;
import com.terryscape.game.animation.AnimationComponent;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.projectile.ProjectileFactory;

// TODO Probably should move into a content module?
public class StandardMagicCombatHit extends StandardCombatFormulaHit {

    private final DamageType damageType;

    private final String attackAnimationId;

    public StandardMagicCombatHit(DamageType damageType, String attackAnimationId) {
        this.damageType = damageType;
        this.attackAnimationId = attackAnimationId;
    }

    @Override
    public void onRegistered(CombatComponent attacker, CombatComponent victim, ProjectileFactory projectileFactory) {
        attacker.getEntity().getComponentOrThrow(AnimationComponent.class).setPlayingAnimation(attackAnimationId);

        // TODO Better way of creating projectiles, this shouldn't be needed.
        var projectileName = damageType == DamageType.FIRE ? "basic_fire_strike" : "basic_air_strike";
        projectileFactory.createRegisteredProjectile(projectileName, projectileComponent -> {
            projectileComponent.setEntitySource(attacker.getEntity());
            projectileComponent.setEntityTarget(victim.getEntity());
            projectileComponent.setLifeSpan(calculateHitDelayTicks(attacker, victim));
        });
    }

    @Override
    public int calculateHitDelayTicks(CombatComponent attacker, CombatComponent victim) {
        var attackerWorldCoordinate = attacker.getEntity().getComponentOrThrow(MovementComponent.class).getWorldCoordinate();
        var victimWorldCoordinate = victim.getEntity().getComponentOrThrow(MovementComponent.class).getWorldCoordinate();

        var possibleTime = Math.round(attackerWorldCoordinate.tileDistance(victimWorldCoordinate) / 2f);
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
