package com.terryscape.cache.item;

import com.terryscape.game.combat.DamageType;

public interface WeaponItemDefinition {

    DamageType getDamageType();

    String getMainHandAttackAnimation();

    String getOffHandAttackAnimation();

}
