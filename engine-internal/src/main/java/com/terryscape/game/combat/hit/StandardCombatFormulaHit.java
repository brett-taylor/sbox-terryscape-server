package com.terryscape.game.combat.hit;

import com.terryscape.game.combat.*;
import com.terryscape.game.combat.health.DamageInformation;
import com.terryscape.game.combat.health.HealthComponent;
import com.terryscape.game.diceroll.CombatDiceRoll;

public abstract class StandardCombatFormulaHit implements CombatHit {

    protected abstract DamageType getDamageType();

    @Override
    public void executeHit(CombatComponent attacker, CombatComponent victim, CombatDiceRoll combatDiceRoll) {
        var playerSkillsComponent = attacker.getEntity().getComponentOrThrow(CombatSkillsProviderComponent.class);
        var playerBonusesProviderComponent = attacker.getEntity().getComponentOrThrow(CombatBonusesProviderComponent.class);
        var victimSkills = victim.getEntity().getComponentOrThrow(CombatSkillsProviderComponent.class);
        var victimBonuses = victim.getEntity().getComponentOrThrow(CombatBonusesProviderComponent.class);
        var didPassAccuracyRoll = combatDiceRoll.rollHitChance(getDamageType(), playerSkillsComponent, playerBonusesProviderComponent, victimSkills, victimBonuses);

        var damageInformation = new DamageInformation().setType(getDamageType());

        if (didPassAccuracyRoll) {
            damageInformation.setAmount(combatDiceRoll.rollDamage(playerSkillsComponent, playerBonusesProviderComponent));
        } else {
            damageInformation.setAmount(0).setBlocked(true);
        }

        victim.getEntity().getComponentOrThrow(HealthComponent.class).takeDamage(damageInformation);
    }

}
