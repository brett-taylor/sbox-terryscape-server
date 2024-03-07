package com.terryscape.game.diceroll;

import com.terryscape.game.combat.CombatBonusesProviderComponent;
import com.terryscape.game.combat.CombatSkillsProviderComponent;
import com.terryscape.game.combat.DamageType;

public interface CombatDiceRoll {

    boolean rollHitChance(DamageType damageType,
                          CombatSkillsProviderComponent attackerSkills,
                          CombatBonusesProviderComponent attackerBonuses,
                          CombatSkillsProviderComponent victimSkills,
                          CombatBonusesProviderComponent victimBonuses);

    int rollDamage(CombatSkillsProviderComponent combatSkillsProviderComponent, CombatBonusesProviderComponent combatBonusesProviderComponent);

    int calculateMaxHit(CombatSkillsProviderComponent combatSkillsProviderComponent, CombatBonusesProviderComponent combatBonusesProviderComponent);
}
