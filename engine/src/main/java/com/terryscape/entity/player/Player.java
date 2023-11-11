package com.terryscape.entity.player;

import com.terryscape.entity.Entity;
import com.terryscape.net.Client;
import com.terryscape.game.chat.PlayerChat;
import com.terryscape.game.equipment.PlayerEquipment;
import com.terryscape.game.item.FixedSizeItemContainer;
import com.terryscape.game.movement.PlayerMovement;

public interface Player extends Entity {

    Client getClient();

    String getUsername();

    PlayerChat getPlayerChat();

    PlayerMovement getPlayerMovement();

    FixedSizeItemContainer getPlayerInventory();

    PlayerEquipment getPlayerEquipment();
}
