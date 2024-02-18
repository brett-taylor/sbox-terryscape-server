package com.terryscape.game.equipment;

import com.google.inject.Singleton;
import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.game.interfaces.InterfaceActionHandler;
import com.terryscape.net.Client;
import com.terryscape.net.IncomingPacket;

import java.nio.ByteBuffer;

@Singleton
public class PlayerEquipmentInterfaceHandler implements InterfaceActionHandler {

    @Override
    public String getInterfaceId() {
        return "equipment";
    }

    @Override
    public void handleAction(Client client, String interfaceId, String interfaceAction, ByteBuffer packet) {
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
