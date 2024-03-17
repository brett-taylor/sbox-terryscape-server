package com.terryscape.game.world;

import com.google.inject.Singleton;
import com.terryscape.game.world.WorldClock;

@Singleton
public class WorldClockImpl implements WorldClock {

    private long tickCount = 0;

    @Override
    public long getNowTick() {
        return tickCount;
    }

    public void incrementTickCount() {
        tickCount += 1;
    }
}
