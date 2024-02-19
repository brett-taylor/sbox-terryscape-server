package com.terryscape.cache.item;

import com.terryscape.game.combat.health.DamageType;
import com.terryscape.game.equipment.EquipmentSlot;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface ClothingDefinition extends ItemDefinition {

    List<Pair<DamageType, Integer>> getBonuses();

    EquipmentSlot getSlot();
}
