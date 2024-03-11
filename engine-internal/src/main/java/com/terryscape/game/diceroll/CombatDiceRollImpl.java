package com.terryscape.game.diceroll;

import com.google.inject.Singleton;
import com.terryscape.game.combat.CombatBonusesProviderComponent;
import com.terryscape.game.combat.CombatSkillsProviderComponent;
import com.terryscape.game.combat.DamageType;
import com.terryscape.maths.RandomUtil;

@Singleton
public class CombatDiceRollImpl implements CombatDiceRoll {

    @Override
    public boolean rollHitChance(DamageType damageType,
                                 CombatSkillsProviderComponent attackerSkills,
                                 CombatBonusesProviderComponent attackerBonuses,
                                 CombatSkillsProviderComponent victimSkills,
                                 CombatBonusesProviderComponent victimBonuses) {

        var attackRoll = calculateAttackRoll(damageType, attackerSkills, attackerBonuses);
        var defenceRoll = calculateDefenceRoll(damageType, victimSkills, victimBonuses);

        var hitChance = calculateHitChance(attackRoll, defenceRoll);
        return RandomUtil.random01() < hitChance;
    }

    @Override
    public int rollDamage(CombatSkillsProviderComponent combatSkillsProviderComponent, CombatBonusesProviderComponent combatBonusesProviderComponent) {
        var maxHit = calculateMaxHit(combatSkillsProviderComponent, combatBonusesProviderComponent);

        return RandomUtil.randomNumber(0, maxHit);
    }

    @Override
    public int calculateMaxHit(CombatSkillsProviderComponent combatSkillsProviderComponent, CombatBonusesProviderComponent combatBonusesProviderComponent) {
        var effectiveStrength = calculateEffectiveStrengthLevel(combatSkillsProviderComponent);
        var strengthBonus = combatBonusesProviderComponent.getStrengthMelee() + 64f;
        var a = effectiveStrength * strengthBonus;
        var b = a + 320f;
        var c = b / 640f;

        return (int) Math.floor(c * 10);
    }

    private int calculateEffectiveAttackLevel(CombatSkillsProviderComponent combatSkillsProviderComponent) {
        var attack = combatSkillsProviderComponent.getAttack();
        return attack + 11;
    }

    private int calculateEffectiveStrengthLevel(CombatSkillsProviderComponent combatSkillsProviderComponent) {
        var strength = combatSkillsProviderComponent.getStrength();
        return strength + 11;
    }

    private int calculateAttackRoll(DamageType damageType, CombatSkillsProviderComponent attackerSkills, CombatBonusesProviderComponent attackerBonuses) {
        var effectiveAttack = calculateEffectiveAttackLevel(attackerSkills);
        var equipmentAttackBonus = getEquipmentAttackBonus(damageType, attackerBonuses);
        return (int) Math.floor(effectiveAttack * (equipmentAttackBonus + 64));
    }

    private float getEquipmentAttackBonus(DamageType damageType, CombatBonusesProviderComponent combatBonuses) {
        return switch (damageType) {
            case STAB -> combatBonuses.getOffensiveStab();
            case SLASH -> combatBonuses.getOffensiveSlash();
            case AIR -> combatBonuses.getOffensiveAir();
            case FIRE -> combatBonuses.getOffensiveFire();
            case TYPELESS -> 0f;
        };
    }

    private int calculateDefenceRoll(DamageType damageType, CombatSkillsProviderComponent victimSkills, CombatBonusesProviderComponent victimBonuses) {
        var a = victimSkills.getDefence() + 9f;
        var b = getEquipmentDefenceBonus(damageType, victimBonuses) + 64f;
        return (int) Math.floor(a * b);
    }

    private float getEquipmentDefenceBonus(DamageType damageType, CombatBonusesProviderComponent combatBonuses) {
        return switch (damageType) {
            case STAB -> combatBonuses.getDefensiveStab();
            case SLASH -> combatBonuses.getDefensiveSlash();
            case AIR -> combatBonuses.getDefensiveAir();
            case FIRE -> combatBonuses.getDefensiveFire();
            case TYPELESS -> 0f;
        };
    }

    private float calculateHitChance(float attackRoll, float defenceRoll) {
        if (attackRoll > defenceRoll) {
            var numerator = defenceRoll + 2;
            var denominator = 2 * (attackRoll + 1);
            return 1 - (numerator / denominator);
        } else {
            var denominator = 2 * (defenceRoll + 1);
            return attackRoll / denominator;
        }
    }
}
