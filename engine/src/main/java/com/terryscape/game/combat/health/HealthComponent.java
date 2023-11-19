package com.terryscape.game.combat.health;

import com.terryscape.entity.component.NetworkedEntityComponent;

public interface HealthComponent extends NetworkedEntityComponent {

    int getMaxHealth();

    void setMaxHealth(int newMaxHealth);

    int getHealth();

    void setHealth(int newHealth);

    void takeDamage(DamageInformation damageInformation);

}
