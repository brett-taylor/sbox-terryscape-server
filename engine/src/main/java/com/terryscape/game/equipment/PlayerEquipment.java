package com.terryscape.game.equipment;

import com.terryscape.cache.ItemDefinition;

import java.io.OutputStream;
import java.util.Optional;

public interface PlayerEquipment {

    Optional<ItemDefinition> getSlot(EquipmentSlot equipmentSlot);

    void setSlot(EquipmentSlot equipmentSlot, ItemDefinition item);

    void removeSlot(EquipmentSlot equipmentSlot);

    void writeToPacket(OutputStream packet);
}
