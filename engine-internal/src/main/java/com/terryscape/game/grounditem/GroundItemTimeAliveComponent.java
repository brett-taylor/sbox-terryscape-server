package com.terryscape.game.grounditem;

import com.terryscape.entity.component.BaseEntityComponent;

public class GroundItemTimeAliveComponent extends BaseEntityComponent {

    private static final int GROUND_ITEM_TIME_ALIVE_TICKS = 120;

    private int ticksLeftAlive;

    public GroundItemTimeAliveComponent() {
        this.ticksLeftAlive = GROUND_ITEM_TIME_ALIVE_TICKS;
    }

    public int getTicksLeftAlive() {
        return ticksLeftAlive;
    }

    public void decrementTicksLeftAlive() {
        ticksLeftAlive -= 1;
    }
}
