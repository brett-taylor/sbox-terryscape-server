package com.terryscape.game.combat;

import com.terryscape.game.diceroll.CombatDiceRoll;

public interface CombatScript {

    boolean isInRange(CombatComponent victim);

    /**
     * @return true if an attack was performed
     */
    boolean attack(CombatComponent attacker, CombatComponent victim, CombatDiceRoll combatDiceRoll);

}
