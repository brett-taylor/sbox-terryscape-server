package com.terryscape.game.item;

import com.terryscape.cache.ItemDefinition;
import com.terryscape.net.OutgoingPacket;
import org.apache.commons.lang3.ArrayUtils;

import java.io.OutputStream;
import java.util.Optional;

public abstract class FixedSizeItemContainer {

    private final ItemDefinition[] items;

    public FixedSizeItemContainer() {
        this.items = new ItemDefinition[getSize()];
    }

    public abstract int getSize();

    public void addItem(ItemDefinition item) {
        var freeSpot = getNextFreeSpot();
        if (freeSpot == null) {
            return;
        }

        this.items[freeSpot] = item;
    }

    public Optional<ItemDefinition> getItemAt(int slotNumber) {
        return Optional.ofNullable(ArrayUtils.get(items, slotNumber, null));
    }

    public void addItemAt(int slotNumber, ItemDefinition item) {
        if (ArrayUtils.isArrayIndexValid(items, slotNumber)) {
            items[slotNumber] = item;
        }
    }

    public void removeItemAt(int slotNumber) {
        if (ArrayUtils.isArrayIndexValid(items, slotNumber)) {
            items[slotNumber] = null;
        }
    }

    public void writeToPacket(OutputStream packet) {
        OutgoingPacket.writeInt32(packet, items.length);
        for (var item : items) {
            OutgoingPacket.writeString(packet, item == null ? "" : item.getId());
        }
    }

    private Integer getNextFreeSpot() {
        for (int i = 0; i < items.length; i++) {
            if (items[i] == null) {
                return i;
            }
        }

        return null;
    }
}
