package com.terryscape.game.combat;

import com.terryscape.entity.component.EntityComponent;

public interface CombatComponent extends EntityComponent {

    void setCombatScript(CombatScript combatScript);

    boolean isInCombat();

    void ensureCooldownOfAtLeast(int ticks);

    void attack(CombatComponent victim);

    void stopAttacking();

    void registerAttack(CombatComponent victim, CombatHit combatHit);
}
