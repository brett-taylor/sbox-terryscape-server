package com.terryscape.game.task.step.impl;

import com.terryscape.game.task.step.TaskStep;

public class WaitTaskStep extends TaskStep {

    public static WaitTaskStep ticks(int ticks) {
        return new WaitTaskStep(ticks);
    }

    private int ticksToWait;

    private WaitTaskStep(int ticks) {
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
