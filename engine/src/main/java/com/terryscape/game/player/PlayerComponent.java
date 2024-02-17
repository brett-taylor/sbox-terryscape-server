package com.terryscape.game.player;

import com.terryscape.entity.component.NetworkedEntityComponent;
import com.terryscape.game.appearance.HumanoidGender;
import com.terryscape.game.equipment.PlayerEquipment;
import com.terryscape.game.item.FixedSizeItemContainer;
import com.terryscape.net.Client;

public interface PlayerComponent extends NetworkedEntityComponent {

    Client getClient();

    String getUsername();

    FixedSizeItemContainer getInventory();

    PlayerEquipment getEquipment();

    void setGender(HumanoidGender gender);

    HumanoidGender getGender();
}
