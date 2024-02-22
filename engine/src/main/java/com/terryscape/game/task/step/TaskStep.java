package com.terryscape.game.task.step;

public abstract class TaskStep {

    private boolean failed;

    public void firstTick() {
    }

    public void tick() {
    }

    public abstract boolean isFinished();

    public boolean hasFailed() {
        return failed;
    }

    public void failed() {
        failed = true;
    }

    public void cancel() {
    }
}
