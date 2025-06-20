package com.terryscape.game.equipment;

import com.terryscape.cache.item.ItemDefinition;
import com.terryscape.game.item.FixedSizeItemContainer;
import com.terryscape.game.item.ItemContainerItem;

import java.util.Optional;

public class PlayerEquipmentImpl extends FixedSizeItemContainer implements PlayerEquipment {
    @Override
    public int getSize() {
        return 7;
    }

    @Override
    public Optional<ItemContainerItem> getSlot(EquipmentSlot equipmentSlot) {
        return getItemAt(equipmentSlot.getSlotId());
    }

    @Override
    public ItemContainerItem getSlotOrThrow(EquipmentSlot equipmentSlot) {
        var slot = getSlot(equipmentSlot);
        if (slot.isEmpty()) {
            throw new RuntimeException("No equipped item for slot %s.".formatted(equipmentSlot));
        }

        return slot.orElseThrow();
    }

    @Override
    public void setSlot(EquipmentSlot equipmentSlot, ItemDefinition item, int quantity) {
        addItemAt(equipmentSlot.getSlotId(), item, quantity);
    }

    @Override
    public void removeSlot(EquipmentSlot equipmentSlot) {
        removeItemAt(equipmentSlot.getSlotId());
    }
}
