package com.terryscape.game.player;

import com.terryscape.entity.component.NetworkedEntityComponent;
import com.terryscape.game.combat.CombatBonusesProviderComponent;

public interface PlayerBonusesProviderComponent extends CombatBonusesProviderComponent, NetworkedEntityComponent {

    float getWeight();

}
