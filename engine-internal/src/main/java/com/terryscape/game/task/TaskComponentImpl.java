package com.terryscape.game.task;

import com.terryscape.entity.Entity;
import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.game.task.step.Step;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TaskComponentImpl extends BaseEntityComponent implements TaskComponent {

    private TaskImpl runningTask;

    public TaskComponentImpl(Entity entity) {
        super(entity);
    }

    @Override
    public Task setTask(Step... steps) {
        Queue<Step> queue = new LinkedList<>(List.of(steps));
        runningTask = new TaskImpl(queue);
        return runningTask;
    }

    @Override
    public void tick() {
        if (runningTask == null) {
            return;
        }

        runningTask.tick();

        if (runningTask.isFinished()) {
            runningTask = null;
        }
    }
}
