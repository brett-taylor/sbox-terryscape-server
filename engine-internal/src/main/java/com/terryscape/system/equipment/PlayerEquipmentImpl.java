package com.terryscape.system.equipment;

import com.terryscape.cache.ItemDefinition;
import com.terryscape.system.item.FixedSizeItemContainer;

import java.util.Optional;

public class PlayerEquipmentImpl extends FixedSizeItemContainer implements PlayerEquipment {
    @Override
    public int getSize() {
        return 2;
    }

    @Override
    public Optional<ItemDefinition> getSlot(EquipmentSlot equipmentSlot) {
        return getItemAt(equipmentSlot.getSlotId());
    }

    @Override
    public void setSlot(EquipmentSlot equipmentSlot, ItemDefinition item) {
        addItemAt(equipmentSlot.getSlotId(), item);
    }

    @Override
    public void removeSlot(EquipmentSlot equipmentSlot) {
        removeItemAt(equipmentSlot.getSlotId());
    }
}
