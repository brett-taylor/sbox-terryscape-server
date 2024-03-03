package com.terryscape.game.equipment;

import com.google.inject.Singleton;
import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.game.interfaces.InterfaceActionHandler;
import com.terryscape.net.Client;
import com.terryscape.net.IncomingPacket;

import java.nio.ByteBuffer;
import java.util.Set;

@Singleton
public class PlayerEquipmentInterfaceActionHandler implements InterfaceActionHandler {

    @Override
    public Set<String> getInterfaceId() {
        return Set.of("equipment");
    }

    @Override
    public void handleAction(Client client, String interfaceId, String interfaceAction, ByteBuffer packet) {
        var slotNumber = IncomingPacket.readInt32(packet);
        var slot = EquipmentSlot.parseFromSlotId(slotNumber);

        var player = client.getPlayer().orElseThrow();
        var playerEquipment = player.getEquipment();
        var playerInventory = player.getInventory();

        var itemOptional = playerEquipment.getSlot(slot);
        if (itemOptional.isEmpty()) {
            return;
        }

        var item = itemOptional.get();
        if (interfaceAction.equals("item_remove")) {
            playerEquipment.removeSlot(slot);
            playerInventory.addItem(item.getItemDefinition(), item.getQuantity());
        }

        if (interfaceAction.equals("item_examine")) {
            player.getEntity().getComponentOrThrow(PlayerChatComponent.class).sendGameMessage(item.getItemDefinition().getDescription());
        }
    }

}
