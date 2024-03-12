package com.terryscape.game.combat;

import com.terryscape.game.diceroll.CombatDiceRoll;

public interface CombatHit {

    void onRegistered(CombatComponent attacker, CombatComponent victim);

    int calculateHitDelayTicks(CombatComponent attacker, CombatComponent victim);

    void executeHit(CombatComponent attacker, CombatComponent victim, CombatDiceRoll combatDiceRoll);

}
