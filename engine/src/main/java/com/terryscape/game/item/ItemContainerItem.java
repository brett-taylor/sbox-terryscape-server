package com.terryscape.game.item;

import com.terryscape.cache.item.ItemDefinition;
import com.terryscape.net.OutgoingPacket;
import com.terryscape.net.PacketSerializable;

import java.io.OutputStream;

public class ItemContainerItem implements PacketSerializable {

    private final ItemDefinition itemDefinition;

    private final int quantity;

    public ItemContainerItem(ItemDefinition itemDefinition, int quantity) {
        this.itemDefinition = itemDefinition;
        this.quantity = quantity;
    }

    public ItemDefinition getItemDefinition() {
        return itemDefinition;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public void writeToPacket(OutputStream packet) {
        OutgoingPacket.writeString(packet, getItemDefinition().getId());
        OutgoingPacket.writeInt32(packet, getQuantity());
    }
}
