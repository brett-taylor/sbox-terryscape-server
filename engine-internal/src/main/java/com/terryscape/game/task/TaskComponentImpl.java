package com.terryscape.game.task;

import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.game.task.step.TaskStep;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

public class TaskComponentImpl extends BaseEntityComponent implements TaskComponent {

    private TaskImpl runningPrimaryTask;

    @Override
    public Optional<Task> setCancellablePrimaryTask(TaskStep... taskSteps) {
        var task = setPrimaryTaskInternal(taskSteps);
        if (task == null) {
            return Optional.empty();
        }

        task.setCancellable(true);
        return Optional.of(task);
    }

    @Override
    public Optional<Task> setPrimaryTask(TaskStep... taskSteps) {
        var task = setPrimaryTaskInternal(taskSteps);
        if (task == null) {
            return Optional.empty();
        }

        task.setCancellable(false);
        return Optional.of(task);
    }

    @Override
    public boolean cancelPrimaryTask() {
        if (!hasPrimaryTask()) {
            return true;
        }

        if (runningPrimaryTask.isCancellable()) {
            runningPrimaryTask.cancel();
            runningPrimaryTask.onFinished();
            runningPrimaryTask = null;
            return true;
        }

        return false;
    }

    @Override
    public boolean hasPrimaryTask() {
        return runningPrimaryTask != null;
    }

    @Override
    public Optional<Task> getPrimaryTask() {
        return Optional.ofNullable(runningPrimaryTask);
    }

    @Override
    public void tick() {
        if (!hasPrimaryTask()) {
            return;
        }

        runningPrimaryTask.tick();

        if (runningPrimaryTask.isFinished()) {
            runningPrimaryTask.onFinished();
            runningPrimaryTask = null;
        }
    }

    private TaskImpl setPrimaryTaskInternal(TaskStep... taskSteps) {
        var cancelledPrimaryTask = cancelPrimaryTask();
        if (!cancelledPrimaryTask) {
            return null;
        }

        Queue<TaskStep> queue = new LinkedList<>(List.of(taskSteps));
        runningPrimaryTask = new TaskImpl(queue);

        runningPrimaryTask.tick();
        return runningPrimaryTask;
    }
}
