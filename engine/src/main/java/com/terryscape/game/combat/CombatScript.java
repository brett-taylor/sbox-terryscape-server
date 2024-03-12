package com.terryscape.game.combat;

import com.terryscape.game.diceroll.CombatDiceRoll;

public interface CombatScript {

    int range();

    /**
     * @return true if an attack was performed
     */
    boolean attack(CombatComponent attacker, CombatComponent victim, CombatDiceRoll combatDiceRoll);

}
