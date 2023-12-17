package com.terryscape.game.task;

import com.terryscape.entity.Entity;
import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.game.task.step.Step;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

public class TaskComponentImpl extends BaseEntityComponent implements TaskComponent {

    private TaskImpl runningPrimaryTask;

    public TaskComponentImpl(Entity entity) {
        super(entity);
    }

    @Override
    public Optional<Task> setCancellablePrimaryTask(Step... steps) {
        var task = setPrimaryTaskInternal(steps);
        if (task == null) {
            return Optional.empty();
        }

        task.setCancellable(true);
        return Optional.of(task);
    }

    @Override
    public Optional<Task> setPrimaryTask(Step... steps) {
        var task = setPrimaryTaskInternal(steps);
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

    private TaskImpl setPrimaryTaskInternal(Step... steps) {
        var cancelledPrimaryTask = cancelPrimaryTask();
        if (!cancelledPrimaryTask) {
            return null;
        }

        Queue<Step> queue = new LinkedList<>(List.of(steps));
        runningPrimaryTask = new TaskImpl(queue);

        runningPrimaryTask.tick();
        return runningPrimaryTask;
    }
}
