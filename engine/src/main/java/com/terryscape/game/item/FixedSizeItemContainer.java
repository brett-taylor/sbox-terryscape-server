package com.terryscape.game.item;

import com.terryscape.cache.item.ItemDefinition;
import com.terryscape.net.OutgoingPacket;
import com.terryscape.net.PacketSerializable;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

public abstract class FixedSizeItemContainer implements PacketSerializable {

    // TODO: This needs to be swapped to Longs

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

    public boolean hasFreeSlots(int inclusiveQuantityOfFreeSlots) {
        return inclusiveQuantityOfFreeSlots <= getFreeSlotCount();
    }

    public int getQuantityOfItem(ItemDefinition itemDefinition) {
        if (itemDefinition.isStackable()) {
            return Arrays.stream(items)
                .filter(Objects::nonNull)
                .filter(itemContainerItem -> itemContainerItem.getItemDefinition() == itemDefinition)
                .map(ItemContainerItem::getQuantity)
                .findFirst()
                .orElse(0);
        }

        return (int) Arrays.stream(items)
            .filter(Objects::nonNull)
            .filter(itemContainerItem -> itemContainerItem.getItemDefinition() == itemDefinition)
            .count();
    }

    public Optional<ItemContainerItem> getItemAt(int slotNumber) {
        return Optional.ofNullable(items[slotNumber]);
    }

    public void addItemAt(int slotNumber, ItemDefinition item, int quantity) {
        items[slotNumber] = new ItemContainerItem(item, quantity);
    }

    public void addItem(ItemDefinition itemDefinition, int quantity) {
        var slotContainingItem = getFirstSlotContainingItem(itemDefinition);
        if (itemDefinition.isStackable() && slotContainingItem != null) {
            var currentItem = getItemAt(slotContainingItem).orElseThrow();

            addItemAt(slotContainingItem, itemDefinition, quantity + currentItem.getQuantity());
            return;
        }

        if (itemDefinition.isStackable()) {
            var freeSpot = getNextFreeSpot();
            if (freeSpot == null) {
                return;
            }

            this.items[freeSpot] = new ItemContainerItem(itemDefinition, quantity);
            return;
        }

        for (int i = 0; i < quantity; i++) {
            var freeSpot = getNextFreeSpot();
            if (freeSpot == null) {
                return;
            }

            this.items[freeSpot] = new ItemContainerItem(itemDefinition, 1);
        }
    }

    public boolean hasItem(ItemDefinition itemDefinition) {
        return getFirstSlotContainingItem(itemDefinition) != null;
    }

    public void removeItemAt(int slotNumber) {
        items[slotNumber] = null;
    }

    public boolean removeItemOfTypeAndQuantity(ItemDefinition itemDefinition, int quantity) {
        var quantityLeft = getQuantityOfItem(itemDefinition) - quantity;
        if (quantityLeft < 0) {
            return false;
        }

        if (itemDefinition.isStackable()) {
            var slot = getFirstSlotContainingItem(itemDefinition);
            removeItemAt(slot);

            if (quantityLeft > 1) {
                addItemAt(slot, itemDefinition, quantityLeft);
            }
        } else {
            var slotsToRemove = IntStream.range(0, items.length)
                .filter(i -> items[i] != null && items[i].getItemDefinition() == itemDefinition)
                .boxed()
                .limit(quantity);

            slotsToRemove.forEach(this::removeItemAt);
        }

        return true;
    }

    public List<ItemContainerItem> getAllItems() {
        return Arrays.stream(items)
            .filter(Objects::nonNull)
            .toList();
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
