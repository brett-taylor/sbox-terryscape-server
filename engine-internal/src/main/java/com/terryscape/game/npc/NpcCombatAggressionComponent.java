package com.terryscape.game.npc;

import com.terryscape.entity.component.BaseEntityComponent;

public class NpcCombatAggressionComponent extends BaseEntityComponent {

    private int ticksSinceLastAttack;

    private int ticksSinceLastAttacked;

    public int getTicksSinceLastAttack() {
        return ticksSinceLastAttack;
    }

    public void setTicksSinceLastAttack(int ticksSinceLastAttack) {
        this.ticksSinceLastAttack = ticksSinceLastAttack;
    }

    public int getTicksSinceLastAttacked() {
        return ticksSinceLastAttacked;
    }

    public void setTicksSinceLastAttacked(int ticksSinceLastAttacked) {
        this.ticksSinceLastAttacked = ticksSinceLastAttacked;
    }
}
