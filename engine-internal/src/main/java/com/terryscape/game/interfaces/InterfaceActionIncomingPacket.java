package com.terryscape.game.interfaces;

import com.google.inject.Singleton;
import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.net.Client;
import com.terryscape.net.IncomingPacket;
import com.terryscape.game.equipment.EquipmentSlot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.ByteBuffer;

@Singleton
public class InterfaceActionIncomingPacket implements IncomingPacket {

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

            var player = client.getPlayer().orElseThrow();
            var playerEquipment = player.getEquipment();
            var playerInventory = player.getInventory();
            var item = playerInventory.getItemAt(slotNumber).orElseThrow();

            if (interfaceAction.equals("item_main_hand")) {
                playerInventory.removeItemAt(slotNumber);
                playerEquipment.setSlot(EquipmentSlot.MAIN_HAND, item);
            }

            if (interfaceAction.equals("item_off_hand")) {
                playerInventory.removeItemAt(slotNumber);
                playerEquipment.setSlot(EquipmentSlot.OFF_HAND, item);
            }

            if (interfaceAction.equals("item_examine")) {
                player.getEntity().getComponentOrThrow(PlayerChatComponent.class).sendGameMessage(item.getDescription());
            }
        }

        if (interfaceId.equals("equipment")) {
            var slotNumber = IncomingPacket.readInt32(packet);
            var slot = EquipmentSlot.tryParseFromSlotId(slotNumber).orElseThrow();

            var player = client.getPlayer().orElseThrow();
            var playerEquipment = player.getEquipment();
            var playerInventory = player.getInventory();
            var item = playerEquipment.getSlot(slot).orElseThrow();


            if (interfaceAction.equals("item_remove")) {
                playerEquipment.removeSlot(slot);
                playerInventory.addItem(item);
            }

            if (interfaceAction.equals("item_examine")) {
                player.getEntity().getComponentOrThrow(PlayerChatComponent.class).sendGameMessage(item.getDescription());
            }
        }
    }
}
