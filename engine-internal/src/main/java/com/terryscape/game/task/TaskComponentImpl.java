package com.terryscape.game.task;

import com.terryscape.entity.Entity;
import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.game.task.step.Step;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TaskComponentImpl extends BaseEntityComponent implements TaskComponent {

    private TaskImpl runningPrimaryTask;

    public TaskComponentImpl(Entity entity) {
        super(entity);
    }

    @Override
    public Task setPrimaryTask(Step... steps) {
        Queue<Step> queue = new LinkedList<>(List.of(steps));
        runningPrimaryTask = new TaskImpl(queue);
        return runningPrimaryTask;
    }

    @Override
    public boolean hasPrimaryTask() {
        return runningPrimaryTask != null;
    }

    @Override
    public void tick() {
        if (runningPrimaryTask == null) {
            return;
        }

        if (runningPrimaryTask.isCancelled()) {
            runningPrimaryTask.onFinished();
            runningPrimaryTask = null;
            return;
        }

        runningPrimaryTask.tick();

        if (runningPrimaryTask.isFinished()) {
            runningPrimaryTask.onFinished();
            runningPrimaryTask = null;
        }
    }
}
