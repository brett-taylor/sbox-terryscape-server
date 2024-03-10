package com.terryscape.game.task.step.impl;

import com.terryscape.game.task.step.TaskStep;

public class NextTickTaskStep extends TaskStep {

    public static NextTickTaskStep doThis(Runnable runnable) {
        return new NextTickTaskStep(runnable);
    }

    private final Runnable runnable;

    private boolean hasRan = false;

    private NextTickTaskStep(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void firstTick() {
        runnable.run();

        hasRan = true;
    }

    @Override
    public boolean isFinished() {
        return hasRan;
    }
}
