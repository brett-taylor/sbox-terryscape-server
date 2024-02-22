package com.terryscape.game.equipment;

import com.google.inject.Singleton;
import com.terryscape.cache.item.ClothingDefinition;
import com.terryscape.cache.item.ClothingDefinitionImpl;
import com.terryscape.cache.item.WeaponDefinition;
import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.game.combat.CharacterStatsImpl;
import com.terryscape.game.combat.SpecialBarImpl;
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
        var slot = EquipmentSlot.tryParseFromSlotId(slotNumber).orElseThrow();

        var player = client.getPlayer().orElseThrow();
        var playerEquipment = player.getEquipment();
        var playerInventory = player.getInventory();
        var playerStats = player.getEntity().getComponentOrThrow(CharacterStatsImpl.class);
        var item = playerEquipment.getSlot(slot).orElseThrow();


        if (interfaceAction.equals("item_remove")) {
            playerEquipment.removeSlot(slot);
            if(item instanceof ClothingDefinition equipment) {
                equipment.getBonuses().forEach(x -> playerStats.RemoveDefenseBonus(x.getLeft(), x.getRight()));
            } else if(item instanceof WeaponDefinition equipment) {
                equipment.getBonuses().forEach(x -> playerStats.RemoveAttackBonus(x.getLeft(), x.getRight()));
                playerStats.AdjustWeaponProficiency(equipment.getPrimaryAttribute(), -equipment.getPrimaryAttributeBonus());
            }
            playerInventory.addItem(item);
        }

        if (interfaceAction.equals("item_special")) {
            player.getEntity().getComponentOrThrow(SpecialBarImpl.class).setSlot(slot);
        }


        if (interfaceAction.equals("item_examine")) {
            player.getEntity().getComponentOrThrow(PlayerChatComponent.class).sendGameMessage(item.getDescription());
        }
    }

}
