package com.terryscape.game.interfaces;

import com.google.inject.Singleton;
import com.terryscape.net.Client;
import com.terryscape.net.IncomingPacket;
import com.terryscape.game.equipment.EquipmentSlot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.ByteBuffer;

@Singleton
public class InterfaceActionIncomingPacket implements IncomingPacket {

    private static final Logger LOGGER = LogManager.getLogger(InterfaceActionIncomingPacket.class);

    @Override
    public String getPacketName() {
        return "interface_action";
    }

    @Override
    public void handlePacket(Client client, ByteBuffer packet) {
        var interfaceId = IncomingPacket.readString(packet);
        var interfaceAction = IncomingPacket.readString(packet);

        if (interfaceId.equals("inventory")) {
            var slotNumber = IncomingPacket.readInt32(packet);

            var playerEquipment = client.getPlayer().orElseThrow().getEquipment();
            var playerInventory = client.getPlayer().orElseThrow().getInventory();
            var item = playerInventory.getItemAt(slotNumber).orElseThrow();
            playerInventory.removeItemAt(slotNumber);

            if (interfaceAction.equals("item_action_one")) {
                playerEquipment.setSlot(EquipmentSlot.MAIN_HAND, item);
                LOGGER.info("Main Hand Item %s".formatted(item.getName()));
            }

            if (interfaceAction.equals("item_action_two")) {
                playerEquipment.setSlot(EquipmentSlot.OFF_HAND, item);
                LOGGER.info("Off Hand Item %s".formatted(item.getName()));
            }
        }

        if (interfaceId.equals("equipment")) {
            var slotNumber = IncomingPacket.readInt32(packet);
            var slot = EquipmentSlot.tryParseFromSlotId(slotNumber).orElseThrow();

            var playerEquipment = client.getPlayer().orElseThrow().getEquipment();
            var playerInventory = client.getPlayer().orElseThrow().getInventory();
            var item = playerEquipment.getSlot(slot).orElseThrow();
            playerEquipment.removeSlot(slot);
            playerInventory.addItem(item);
        }
    }
}
