package com.terryscape.game.combat;

import com.terryscape.game.diceroll.CombatDiceRoll;

public interface CombatHit {

    int getHitDelayTicks();

    void executeHit(CombatComponent attacker, CombatComponent victim, CombatDiceRoll combatDiceRoll);

}
