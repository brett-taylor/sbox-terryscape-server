package com.terryscape.game.task.step.impl;

import com.terryscape.game.task.step.Step;

public class ImmediateStep extends Step {

    public static ImmediateStep run(Runnable runnable) {
        return new ImmediateStep(runnable);
    }

    private final Runnable runnable;

    private ImmediateStep(Runnable runnable) {
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
