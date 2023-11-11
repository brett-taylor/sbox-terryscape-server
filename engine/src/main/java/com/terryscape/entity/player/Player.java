package com.terryscape.entity.player;

import com.terryscape.entity.Entity;
import com.terryscape.net.Client;
import com.terryscape.system.chat.PlayerChat;
import com.terryscape.system.equipment.PlayerEquipment;
import com.terryscape.system.item.FixedSizeItemContainer;
import com.terryscape.system.movement.PlayerMovement;

public interface Player extends Entity {

    Client getClient();

    String getUsername();

    PlayerChat getPlayerChat();

    PlayerMovement getPlayerMovement();

    FixedSizeItemContainer getPlayerInventory();

    PlayerEquipment getPlayerEquipment();
}
