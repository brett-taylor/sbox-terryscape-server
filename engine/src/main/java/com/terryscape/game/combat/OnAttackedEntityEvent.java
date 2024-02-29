package com.terryscape.game.combat;

import com.terryscape.entity.event.EntityEvent;

public class OnAttackedEntityEvent implements EntityEvent  {

    private final CombatComponent attacker;

    public OnAttackedEntityEvent(CombatComponent attacker) {
        this.attacker = attacker;
    }

    public CombatComponent getAttacker() {
        return attacker;
    }
}
