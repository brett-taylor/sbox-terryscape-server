package com.terryscape.game.equipment;

import com.terryscape.cache.item.ItemDefinition;
import com.terryscape.game.item.ItemContainerItem;

import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

public interface PlayerEquipment {

    Optional<ItemContainerItem> getSlot(EquipmentSlot equipmentSlot);

    void setSlot(EquipmentSlot equipmentSlot, ItemDefinition item, int quantity);

    void removeSlot(EquipmentSlot equipmentSlot);

    void writeToPacket(OutputStream packet);

    List<ItemContainerItem> getAllItems();
}
