package com.terryscape.game.task.step.impl;

import com.terryscape.game.task.step.TaskStep;

public class ImmediateTaskStep extends TaskStep {

    public static ImmediateTaskStep run(Runnable runnable) {
        return new ImmediateTaskStep(runnable);
    }

    private final Runnable runnable;

    private ImmediateTaskStep(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void firstTick() {
        runnable.run();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
