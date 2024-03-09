package com.terryscape.game.specialattack;

import com.terryscape.game.combat.CombatComponent;

import java.util.Set;

public interface SpecialAttackHandler {

    Set<String> getItemIds();

    void attack(CombatComponent attacker, CombatComponent victim);

    float getSpecialAttackPowerNeeded();
}
