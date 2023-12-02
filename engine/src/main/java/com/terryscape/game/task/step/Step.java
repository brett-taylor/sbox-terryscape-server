package com.terryscape.game.task.step;

public interface Step {

    default void firstTick() {
    }

    default void tick() {
    }

    boolean isFinished();

}
