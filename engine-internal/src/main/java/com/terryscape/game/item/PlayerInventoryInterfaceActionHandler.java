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

        var itemOptional = playerInventory.getItemAt(slotNumber);
        if (itemOptional.isEmpty()) {
            return;
        }

        var item = itemOptional.get();
        if (interfaceAction.equals("item_main_hand")) {
            playerInventory.removeItemAt(slotNumber);
            playerEquipment.setSlot(EquipmentSlot.MAIN_HAND, item.getItemDefinition(), item.getQuantity());
        }

        if (interfaceAction.equals("item_off_hand")) {
            playerInventory.removeItemAt(slotNumber);
            playerEquipment.setSlot(EquipmentSlot.OFF_HAND, item.getItemDefinition(), item.getQuantity());
        }

        if (interfaceAction.equals("item_examine")) {
            player.getEntity().getComponentOrThrow(PlayerChatComponent.class).sendGameMessage(item.getItemDefinition().getDescription());
        }
    }
}
