package com.terryscape.game.item;

import com.terryscape.cache.item.ItemDefinition;
import com.terryscape.net.OutgoingPacket;
import com.terryscape.net.PacketSerializable;
import org.apache.commons.lang3.ArrayUtils;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

public abstract class FixedSizeItemContainer implements PacketSerializable {

    private final ItemContainerItem[] items;

    public FixedSizeItemContainer() {
        this.items = new ItemContainerItem[getSize()];
    }

    public abstract int getSize();

    public int getFreeSlotCount() {
        return (int) Arrays.stream(items)
            .filter(Objects::isNull)
            .count();
    }

    public boolean hasItem(ItemDefinition itemDefinition) {
        return getFirstSlotContainingItem(itemDefinition) != null;
    }

    public Optional<ItemContainerItem> getItemAt(int slotNumber) {
        return Optional.ofNullable(items[slotNumber]);
    }

    public void addItemAt(int slotNumber, ItemDefinition item, int quantity) {
        items[slotNumber] = new ItemContainerItem(item, quantity);
    }

    public void removeItemAt(int slotNumber) {
        items[slotNumber] = null;
    }

    public void addItem(ItemDefinition itemDefinition, int quantity) {
        var slotContainingItem = getFirstSlotContainingItem(itemDefinition);
        if (itemDefinition.isStackable() && slotContainingItem != null) {
            var currentItem = getItemAt(slotContainingItem).orElseThrow();

            addItemAt(slotContainingItem, itemDefinition, quantity + currentItem.getQuantity());
            System.err.println(getItemAt(slotContainingItem).orElseThrow().getQuantity());
            return;
        }

        var freeSpot = getNextFreeSpot();
        this.items[freeSpot] = new ItemContainerItem(itemDefinition, quantity);
    }

    private Integer getFirstSlotContainingItem(ItemDefinition itemDefinition) {
        return IntStream.range(0, items.length)
            .filter(i -> items[i] != null && items[i].getItemDefinition() == itemDefinition)
            .boxed()
            .findFirst()
            .orElse(null);
    }

    private Integer getNextFreeSpot() {
        return IntStream.range(0, items.length)
            .filter(i -> items[i] == null)
            .boxed()
            .findFirst()
            .orElse(null);
    }

    @Override
    public void writeToPacket(OutputStream packet) {
        OutgoingPacket.writeInt32(packet, items.length);
        for (var item : items) {
            if (item == null) {
                OutgoingPacket.writeString(packet, null);
            } else {
                OutgoingPacket.writeString(packet, item.getItemDefinition().getId());
                OutgoingPacket.writeInt32(packet, item.getQuantity());
            }
        }
    }
}
