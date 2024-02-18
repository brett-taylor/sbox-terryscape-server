package com.terryscape.game.item;

import com.google.inject.Singleton;
import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.game.equipment.EquipmentSlot;
import com.terryscape.game.interfaces.InterfaceActionHandler;
import com.terryscape.net.Client;
import com.terryscape.net.IncomingPacket;

import java.nio.ByteBuffer;

@Singleton
public class PlayerInventoryInterfaceHandler implements InterfaceActionHandler {

    @Override
    public String getInterfaceId() {
        return "inventory";
    }

    @Override
    public void handleAction(Client client, String interfaceId, String interfaceAction, ByteBuffer packet) {
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
}
