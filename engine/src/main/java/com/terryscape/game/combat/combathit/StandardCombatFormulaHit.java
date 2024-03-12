package com.terryscape.game.combat.combathit;

import com.terryscape.game.combat.*;
import com.terryscape.game.combat.health.DamageInformation;
import com.terryscape.game.combat.health.HealthComponent;
import com.terryscape.game.diceroll.CombatDiceRoll;

// TODO Probably should move into a content module?

public abstract class StandardCombatFormulaHit implements CombatHit {

    protected abstract DamageType getDamageType();

    @Override
    public void executeHit(CombatComponent attacker, CombatComponent victim, CombatDiceRoll combatDiceRoll) {
        var playerSkillsComponent = attacker.getEntity().getComponentOrThrow(CombatSkillsProviderComponent.class);
        var playerBonusesProviderComponent = attacker.getEntity().getComponentOrThrow(CombatBonusesProviderComponent.class);
        var victimSkills = victim.getEntity().getComponentOrThrow(CombatSkillsProviderComponent.class);
        var victimBonuses = victim.getEntity().getComponentOrThrow(CombatBonusesProviderComponent.class);
        var didPassAccuracyRoll = combatDiceRoll.rollHitChance(playerSkillsComponent, playerBonusesProviderComponent, victimSkills, victimBonuses, getDamageType());

        var damageInformation = new DamageInformation().setType(getDamageType());

        if (didPassAccuracyRoll) {
            damageInformation.setAmount(combatDiceRoll.rollDamage(playerSkillsComponent, playerBonusesProviderComponent, getDamageType()));
        } else {
            damageInformation.setAmount(0).setBlocked(true);
        }

        victim.getEntity().getComponentOrThrow(HealthComponent.class).takeDamage(damageInformation);
    }

}
