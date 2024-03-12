package com.terryscape.game.combat;

public interface CombatScript {

    void setOwner(CombatComponent combatComponent);

    int range();

    void attack(CombatComponent victim);
}
