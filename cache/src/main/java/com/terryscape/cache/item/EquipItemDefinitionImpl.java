package com.terryscape.cache.item;

import com.terryscape.game.equipment.EquipmentSlot;

import java.util.Optional;

public class EquipItemDefinitionImpl implements EquipItemDefinition {

    private EquipmentSlot equipmentSlot;

    private WeaponItemDefinition weaponItemDefinition;

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return equipmentSlot;
    }

    public EquipItemDefinitionImpl setEquipmentSlot(EquipmentSlot equipmentSlot) {
        this.equipmentSlot = equipmentSlot;
        return this;
    }

    @Override
    public Optional<WeaponItemDefinition> getWeaponDefinition() {
        return Optional.ofNullable(weaponItemDefinition);
    }

    @Override
    public WeaponItemDefinition getWeaponDefinitionOrThrow() {
        if (weaponItemDefinition == null) {
            throw new RuntimeException("No weapon information for item.");
        }

        return weaponItemDefinition;
    }

    public EquipItemDefinitionImpl setWeaponItemDefinition(WeaponItemDefinition weaponItemDefinition) {
        this.weaponItemDefinition = weaponItemDefinition;
        return this;
    }
}
