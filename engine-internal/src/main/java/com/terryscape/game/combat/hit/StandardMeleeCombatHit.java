package com.terryscape.game.combat.hit;

import com.terryscape.game.combat.*;
import com.terryscape.game.combat.health.DamageInformation;
import com.terryscape.game.combat.health.HealthComponent;
import com.terryscape.game.diceroll.CombatDiceRoll;
import com.terryscape.game.movement.AnimationComponent;

public class StandardMeleeCombatHit implements CombatHit {

    private final DamageType damageType;

    private final String attackAnimationId;

    public StandardMeleeCombatHit(DamageType damageType, String attackAnimationId) {
        this.damageType = damageType;
        this.attackAnimationId = attackAnimationId;
    }

    @Override
    public int getHitDelayTicks() {
        return 0;
    }

    @Override
    public void executeHit(CombatComponent attacker, CombatComponent victim, CombatDiceRoll combatDiceRoll) {
        var playerSkillsComponent = attacker.getEntity().getComponentOrThrow(CombatSkillsProviderComponent.class);
        var playerBonusesProviderComponent = attacker.getEntity().getComponentOrThrow(CombatBonusesProviderComponent.class);
        var victimSkills = victim.getEntity().getComponentOrThrow(CombatSkillsProviderComponent.class);
        var victimBonuses = victim.getEntity().getComponentOrThrow(CombatBonusesProviderComponent.class);
        var didPassAccuracyRoll = combatDiceRoll.rollHitChance(damageType, playerSkillsComponent, playerBonusesProviderComponent, victimSkills, victimBonuses);

        var damageInformation = new DamageInformation().setType(damageType);

        if (didPassAccuracyRoll) {
            damageInformation.setAmount(combatDiceRoll.rollDamage(playerSkillsComponent, playerBonusesProviderComponent));
        } else {
            damageInformation.setAmount(0).setBlocked(true);
        }

        attacker.getEntity().getComponentOrThrow(AnimationComponent.class).playAnimation(attackAnimationId);
        victim.getEntity().getComponentOrThrow(HealthComponent.class).takeDamage(damageInformation);
    }
}
