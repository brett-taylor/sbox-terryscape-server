package com.terryscape.game.task;

public interface Task {

    void cancel();

    /**
     * Will execute the runnable when the task finishes either because it naturally finishes or because it was cancelled.
     */
    void onFinished(Runnable runnable);

}
