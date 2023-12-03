package com.terryscape.game.task;

import java.util.function.Consumer;

public interface Task {

    void cancel();

    boolean isFinished();

    /**
     * Will execute the consumer when the task finishes with a reason why it finished
     */
    void onFinished(Consumer<TaskFinishedReason> consumer);

}
