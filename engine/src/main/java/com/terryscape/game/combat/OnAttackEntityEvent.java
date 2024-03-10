package com.terryscape.game.combat;

import com.terryscape.entity.event.EntityEvent;

public class OnAttackEntityEvent implements EntityEvent  {

    private final CombatComponent victim;

    public OnAttackEntityEvent(CombatComponent victim) {
        this.victim = victim;
    }

    public CombatComponent getVictim() {
        return victim;
    }
}
