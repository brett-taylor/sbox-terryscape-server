package com.terryscape.game.item;

import com.google.inject.Singleton;
import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.game.equipment.EquipmentSlot;
import com.terryscape.game.equipment.PlayerEquipment;
import com.terryscape.game.interfaces.InterfaceActionHandler;
import com.terryscape.game.player.PlayerComponent;
import com.terryscape.net.Client;
import com.terryscape.net.IncomingPacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.ByteBuffer;
import java.util.Set;

@Singleton
public class PlayerInventoryInterfaceActionHandler implements InterfaceActionHandler {

    private static final Logger LOGGER = LogManager.getLogger(PlayerInventoryInterfaceActionHandler.class);

    @Override
    public Set<String> getInterfaceId() {
        return Set.of("inventory");
    }

    @Override
    public void handleAction(Client client, String interfaceId, String interfaceAction, ByteBuffer packet) {
        var slotNumber = IncomingPacket.readInt32(packet);
        var modifyAction = IncomingPacket.readBoolean(packet);

        var player = client.getPlayer().orElseThrow();
        var playerInventory = player.getInventory();

        var itemOptional = playerInventory.getItemAt(slotNumber);
        if (itemOptional.isEmpty()) {
            return;
        }

        var item = itemOptional.get();
        if (interfaceAction.equals("equip")) {
            equipItem(modifyAction, player, slotNumber, item);
        }

        if (interfaceAction.equals("examine")) {
            player.getEntity().getComponentOrThrow(PlayerChatComponent.class).sendGameMessage(item.getItemDefinition().getDescription());
        }

        if (interfaceAction.equals("drop")) {
            playerInventory.removeItemAt(slotNumber);
            var playerChat = player.getEntity().getComponentOrThrow(PlayerChatComponent.class);
            playerChat.sendGameMessage("The %s disintegrates into dust...".formatted(item.getItemDefinition().getName()));
        }
    }

    private void equipItem(boolean modifyAction, PlayerComponent player, int slotNumber, ItemContainerItem item) {
        var equipOptional = item.getItemDefinition().getEquipDefinition();
        if (equipOptional.isEmpty()) {
            LOGGER.warn("Player {} attempted to equip item {} which can not be equipped", player.getUsername(), item.getItemDefinition().getId());
            return;
        }

        var slot = equipOptional.get().getEquipmentSlot();
        if (slot == EquipmentSlot.MAIN_HAND && modifyAction) {
            slot = EquipmentSlot.OFF_HAND;
        }

        var previouslyEquippedItem = player.getEquipment().getSlot(slot);

        player.getInventory().removeItemAt(slotNumber);
        player.getEquipment().setSlot(slot, item.getItemDefinition(), item.getQuantity());

        previouslyEquippedItem.ifPresent(itemContainerItem -> player.getInventory().addItemAt(slotNumber, itemContainerItem.getItemDefinition(), 1));
    }
}
