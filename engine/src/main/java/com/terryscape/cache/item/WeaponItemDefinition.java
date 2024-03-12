package com.terryscape.cache.item;

import com.terryscape.game.combat.DamageType;

public interface WeaponItemDefinition {

    boolean isTwoHanded();

    DamageType getDamageType();

    int getRange();

    int getAttackSpeed();

    String getMainHandAttackAnimation();

    String getOffHandAttackAnimation();

}
