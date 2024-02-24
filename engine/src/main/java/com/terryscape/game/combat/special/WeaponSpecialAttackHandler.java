package com.terryscape.game.combat.special;

import com.terryscape.cache.item.WeaponDefinition;
import com.terryscape.game.combat.CombatComponent;

public interface WeaponSpecialAttackHandler {

    String getItemId();

    boolean attack(CombatComponent attacker, CombatComponent victim);

}
