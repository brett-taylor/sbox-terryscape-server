package com.terryscape.cache.item;

import com.terryscape.game.combat.health.AttackType;
import com.terryscape.game.combat.health.DamageType;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface WeaponDefinition extends ItemDefinition {
    Boolean attack(long currentTick);
    AttackType getPrimaryAttribute();
    int getPrimaryAttributeBonus();
    List<Pair<DamageType, Integer>> getBonuses();
    DamageType getDamageType();
    String getAttackAnimation();
}
