package com.terryscape.game.combat;

import com.terryscape.entity.component.EntityComponent;

public interface CombatComponent extends EntityComponent {

    boolean isInCombat();

    void attack(CombatComponent victim);

    void attackedBy(CombatComponent attacker);

    void ensureCooldownOfAtLeast(int ticks);

}
