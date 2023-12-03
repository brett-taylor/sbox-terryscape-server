package com.terryscape.game.task.step.impl;

import com.terryscape.game.task.step.Step;

public class WaitStep extends Step {

    public static WaitStep ticks(int ticks) {
        return new WaitStep(ticks);
    }

    private int ticksToWait;

    private WaitStep(int ticks) {
        this.ticksToWait = ticks;
    }

    @Override
    public void tick() {
        ticksToWait -= 1;
    }

    @Override
    public boolean isFinished() {
        return ticksToWait <= 0;
    }
}
