package com.terryscape.game.item;

import com.google.inject.Singleton;
import com.terryscape.cache.item.ClothingDefinition;
import com.terryscape.cache.item.WeaponDefinition;
import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.game.combat.CharacterStatsImpl;
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
        var playerStats = player.getEntity().getComponentOrThrow(CharacterStatsImpl.class);
        var item = playerInventory.getItemAt(slotNumber).orElseThrow();

        if (interfaceAction.equals("item_examine")) {
            player.getEntity().getComponentOrThrow(PlayerChatComponent.class).sendGameMessage(item.getDescription());
            return;
        }

        EquipmentSlot equipmentSlot;
        switch (interfaceAction) {
            case "item_main_hand" -> equipmentSlot = EquipmentSlot.MAIN_HAND;
            case "item_off_hand" -> equipmentSlot = EquipmentSlot.OFF_HAND;
            case "item_equip" -> {
                if (item instanceof ClothingDefinition) {
                    equipmentSlot = ((ClothingDefinition) item).getSlot();
                } else {
                    System.err.println("Cannot equip " + item.getName() + " it is not clothing.");
                    return;
                }
            }
            default -> {
                System.err.println("Invalid interfaceAction: " + interfaceAction);
                return;
            }
        }

        playerInventory.removeItemAt(slotNumber);
        var occupiedSlot = playerEquipment.getSlot(equipmentSlot);
        if (occupiedSlot.isPresent()) {
            var removedEquipment = occupiedSlot.get();
            if(removedEquipment instanceof ClothingDefinition equipment) {
                equipment.getBonuses().forEach(x -> playerStats.RemoveDefenseBonus(x.getLeft(), x.getRight()));
            } else if(removedEquipment instanceof WeaponDefinition equipment) {
                equipment.getBonuses().forEach(x -> playerStats.RemoveAttackBonus(x.getLeft(), x.getRight()));
            }
            playerInventory.addItem(removedEquipment);
            playerEquipment.removeSlot(equipmentSlot);
        }
        playerEquipment.setSlot(equipmentSlot, item);

        switch (equipmentSlot) {
            case MAIN_HAND, OFF_HAND -> ((WeaponDefinition)item).getBonuses().forEach(x -> playerStats.AddAttackBonus(x.getLeft(), x.getRight()));
            case TORSO -> ((ClothingDefinition)item).getBonuses().forEach(x -> playerStats.AddDefenseBonus(x.getLeft(), x.getRight()));
        }
    }
}
