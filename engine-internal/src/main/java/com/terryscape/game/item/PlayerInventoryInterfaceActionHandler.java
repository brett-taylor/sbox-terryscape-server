package com.terryscape.game.item;

import com.google.inject.Singleton;
import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.game.equipment.EquipmentSlot;
import com.terryscape.game.interfaces.InterfaceActionHandler;
import com.terryscape.net.Client;
import com.terryscape.net.IncomingPacket;

import java.nio.ByteBuffer;
import java.util.Set;

@Singleton
public class PlayerInventoryInterfaceActionHandler implements InterfaceActionHandler {

    @Override
    public Set<String> getInterfaceId() {
        return Set.of("inventory");
    }

    @Override
    public void handleAction(Client client, String interfaceId, String interfaceAction, ByteBuffer packet) {
        var slotNumber = IncomingPacket.readInt32(packet);

        var player = client.getPlayer().orElseThrow();
        var playerEquipment = player.getEquipment();
        var playerInventory = player.getInventory();
        var item = playerInventory.getItemAt(slotNumber).orElseThrow();

        if (interfaceAction.equals("item_examine")) {
            player.getEntity().getComponentOrThrow(PlayerChatComponent.class).sendGameMessage(item.getDescription());
            return;
        }

        EquipmentSlot equipmentSlot;
        if (interfaceAction.equals("item_main_hand")) {
            equipmentSlot = EquipmentSlot.MAIN_HAND;
        } else if (interfaceAction.equals("item_off_hand")) {
            equipmentSlot = EquipmentSlot.OFF_HAND;
        } else {
            return;
        }

        playerInventory.removeItemAt(slotNumber);
        playerEquipment.getSlot(equipmentSlot).ifPresent(playerInventory::addItem);
        playerEquipment.setSlot(equipmentSlot, item);
    }
}
