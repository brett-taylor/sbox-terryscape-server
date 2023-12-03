package com.terryscape.world;

import com.google.inject.Singleton;

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
