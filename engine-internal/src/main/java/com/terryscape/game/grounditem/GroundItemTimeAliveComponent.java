package com.terryscape.game.grounditem;

import com.terryscape.entity.component.BaseEntityComponent;

public class GroundItemTimeAliveComponent extends BaseEntityComponent {

    private static final int GROUND_ITEM_TIME_ALIVE_TICKS = 120;

    private int ticksLeftAlive = 0;

    @Override
    public void onRegistered() {
        super.onRegistered();

        ticksLeftAlive = GROUND_ITEM_TIME_ALIVE_TICKS;
    }

    @Override
    public void tick() {
        super.tick();

        ticksLeftAlive -= 1;

        if (ticksLeftAlive <= 0) {
            getEntity().delete();
        }
    }
}
