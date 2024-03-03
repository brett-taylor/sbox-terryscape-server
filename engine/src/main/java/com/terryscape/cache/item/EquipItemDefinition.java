package com.terryscape.cache.item;

import com.terryscape.game.equipment.EquipmentSlot;

import java.util.Optional;

public interface EquipItemDefinition {

    EquipmentSlot getEquipmentSlot();

    Optional<WeaponItemDefinition> getWeaponDefinition();

    WeaponItemDefinition getWeaponDefinitionOrThrow();

}
