package com.terryscape.game.diceroll;

import com.terryscape.game.combat.CombatBonusesProviderComponent;
import com.terryscape.game.combat.CombatSkillsProviderComponent;
import com.terryscape.game.combat.DamageType;

public interface CombatDiceRoll {

    boolean rollHitChance(CombatSkillsProviderComponent attackerSkills,
                          CombatBonusesProviderComponent attackerBonuses,
                          CombatSkillsProviderComponent victimSkills,
                          CombatBonusesProviderComponent victimBonuses,
                          DamageType damageType);

    int rollDamage(CombatSkillsProviderComponent attackerSkills, CombatBonusesProviderComponent attackerBonuses, DamageType damageType);

    int calculateMaxHit(CombatSkillsProviderComponent attackerSkills, CombatBonusesProviderComponent attackerBonuses, DamageType damageType);
}
