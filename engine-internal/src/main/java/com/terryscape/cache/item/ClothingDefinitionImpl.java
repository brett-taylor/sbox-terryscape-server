package com.terryscape.cache.item;

import com.terryscape.game.combat.health.DamageType;
import com.terryscape.game.equipment.EquipmentSlot;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class ClothingDefinitionImpl extends ItemDefinitionImpl implements ClothingDefinition {
    private EquipmentSlot slot;
    private List<Pair<DamageType, Integer>> bonuses = new ArrayList<>();

    @Override
    public List<Pair<DamageType, Integer>> getBonuses() {
        return bonuses;
    }

    @Override
    public EquipmentSlot getSlot() {
        return slot;
    }

    public ClothingDefinitionImpl setEquipmentSlot(EquipmentSlot slot){
        this.slot = slot;
        return this;
    }

    public ClothingDefinitionImpl setBonuses(List<Pair<DamageType, Integer>> bonuses){
        this.bonuses = bonuses;
        return this;
    }
}
