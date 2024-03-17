package com.terryscape.game.specialattack;

import com.terryscape.cache.item.ItemDefinition;
import com.terryscape.game.combat.CombatComponent;

import java.util.Set;

public interface SpecialAttackHandler {

    Set<ItemDefinition> getItems();

    void attack(CombatComponent attacker, CombatComponent victim);

    float getSpecialAttackPowerNeeded();
}
