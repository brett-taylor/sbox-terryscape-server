package com.terryscape.game.combat.health;

import com.terryscape.entity.component.EntityComponent;

public interface HealthComponent extends EntityComponent {

    int getMaxHealth();

    void setMaxHealth(int newMaxHealth);

    int getHealth();

    void setHealth(int newHealth);

    boolean isDying();

    void takeDamage(DamageInformation damageInformation);

    void heal(int amount);

    void resetHealthToMax();

}
