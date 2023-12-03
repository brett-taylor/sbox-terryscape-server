package com.terryscape.game.combat;

public interface CombatScript {

    boolean isInRange(CombatComponent victim);

    /**
     * @return true if an attack was performed
     */
    boolean attack(CombatComponent victim);

}
