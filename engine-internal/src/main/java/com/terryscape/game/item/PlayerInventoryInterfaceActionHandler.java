package com.terryscape.game.item;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.item.WeaponItemDefinition;
import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.game.equipment.EquipmentSlot;
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

    private final ItemInteractionDispatcher itemInteractionDispatcher;

    @Inject
    public PlayerInventoryInterfaceActionHandler(ItemInteractionDispatcher itemInteractionDispatcher) {
        this.itemInteractionDispatcher = itemInteractionDispatcher;
    }

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

        // TODO: Can the player interact with items?

        var itemOptional = playerInventory.getItemAt(slotNumber);
        if (itemOptional.isEmpty()) {
            return;
        }

        var item = itemOptional.get();
        if (interfaceAction.equals("equip")) {
            equipItem(modifyAction, player, slotNumber, item);
        }

        if (interfaceAction.equals("interact")) {
            itemInteractionDispatcher.dispatchItemInteraction(client, slotNumber);
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
        var isTwoHanded = equipOptional.get().getWeaponDefinition().map(WeaponItemDefinition::isTwoHanded).orElse(false);
        if (slot == EquipmentSlot.MAIN_HAND && modifyAction && !isTwoHanded) {
            slot = EquipmentSlot.OFF_HAND;
        }

        if (isTwoHanded && !player.getInventory().hasFreeSlots(1)) {
            player.getEntity().getComponentOrThrow(PlayerChatComponent.class)
                .sendGameMessage("You do not have the inventory space to equip your %s.".formatted(item.getItemDefinition().getName()));

            return;
        }

        var previouslyEquippedItem = player.getEquipment().getSlot(slot);

        player.getInventory().removeItemAt(slotNumber);
        player.getEquipment().setSlot(slot, item.getItemDefinition(), item.getQuantity());

        previouslyEquippedItem.ifPresent(itemContainerItem -> player.getInventory().addItemAt(slotNumber, itemContainerItem.getItemDefinition(), 1));

        if (isTwoHanded) {
            var previousEquippedOffhandItem = player.getEquipment().getSlot(EquipmentSlot.OFF_HAND);
            player.getEquipment().removeSlot(EquipmentSlot.OFF_HAND);
            previousEquippedOffhandItem.ifPresent(itemContainerItem -> player.getInventory().addItem(itemContainerItem.getItemDefinition(), 1));
        }
    }
}
