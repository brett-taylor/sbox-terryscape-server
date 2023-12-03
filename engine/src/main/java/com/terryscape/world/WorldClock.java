package com.terryscape.world;

public interface WorldClock {

    /**
     * @return how many ticks have been ran since the world existed
     */
    long getNowTick();

}
