package com.terryscape.game.specialattack;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.game.combat.CombatBonusesProviderComponent;
import com.terryscape.game.combat.CombatComponent;
import com.terryscape.game.combat.CombatSkillsProviderComponent;
import com.terryscape.game.combat.DamageType;
import com.terryscape.game.combat.health.DamageInformation;
import com.terryscape.game.combat.health.HealthComponent;
import com.terryscape.game.diceroll.CombatDiceRoll;

@Singleton
public class SpecialAttackHandlerUtils {

    private final CombatDiceRoll combatDiceRoll;

    @Inject
    public SpecialAttackHandlerUtils(CombatDiceRoll combatDiceRoll) {
        this.combatDiceRoll = combatDiceRoll;
    }

    public boolean rollStandardAccuracyHitChance(CombatComponent attacker, CombatComponent victim, DamageType damageType) {
        var attackerSkills = attacker.getEntity().getComponentOrThrow(CombatSkillsProviderComponent.class);
        var attackerBonuses = attackerSkills.getEntity().getComponentOrThrow(CombatBonusesProviderComponent.class);
        var victimSkills = victim.getEntity().getComponentOrThrow(CombatSkillsProviderComponent.class);
        var victimBonuses = victim.getEntity().getComponentOrThrow(CombatBonusesProviderComponent.class);

        return combatDiceRoll.rollHitChance(attackerSkills, attackerBonuses, victimSkills, victimBonuses, damageType);
    }

    public int rollStandardDamageHit(CombatComponent attacker, DamageType damageType) {
        var attackerSkills = attacker.getEntity().getComponentOrThrow(CombatSkillsProviderComponent.class);
        var attackerBonuses = attackerSkills.getEntity().getComponentOrThrow(CombatBonusesProviderComponent.class);

        return combatDiceRoll.rollDamage(attackerSkills, attackerBonuses, damageType);
    }

    public void showStandardMissedHit(CombatComponent victim, DamageType damageType) {
        var victimHealth = victim.getEntity().getComponentOrThrow(HealthComponent.class);
        var damageInformation = new DamageInformation()
            .setAmount(0)
            .setBlocked(true)
            .setType(damageType);

        victimHealth.takeDamage(damageInformation);
    }

    public void showStandardHit(CombatComponent victim, int amount, DamageType damageType) {
        var victimHealth = victim.getEntity().getComponentOrThrow(HealthComponent.class);
        var damageInformation = new DamageInformation()
            .setAmount(amount)
            .setType(damageType);

        victimHealth.takeDamage(damageInformation);
    }
}
