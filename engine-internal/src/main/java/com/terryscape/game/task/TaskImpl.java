package com.terryscape.game.task;

import com.terryscape.game.task.step.TaskStep;

import java.util.Queue;
import java.util.function.Consumer;

public class TaskImpl implements Task {

    private final Queue<TaskStep> taskSteps;

    private TaskStep currentTaskStep;

    private boolean isCancelled;

    private boolean isFirstTickForStep;

    private boolean hasFailed;

    private boolean cancellable;

    private Consumer<TaskFinishedReason> onFinishedConsumer;

    public TaskImpl(Queue<TaskStep> taskSteps) {
        this.taskSteps = taskSteps;

        getNextStep();
    }

    public void tick() {
        if (isFinished()) {
            return;
        }

        if (isFirstTickForStep) {
            currentTaskStep.firstTick();
            isFirstTickForStep = false;
        }

        currentTaskStep.tick();

        if (currentTaskStep.hasFailed()) {
            hasFailed = true;
            currentTaskStep.cancel();
            return;
        }

        if (currentTaskStep.isFinished()) {
            getNextStep();
        }
    }

    @Override
    public void cancel() {
        if (currentTaskStep != null) {
            currentTaskStep.cancel();
        }

        isCancelled = true;
    }

    @Override
    public boolean isFinished() {
        return hasFailed || isCancelled || (currentTaskStep == null && taskSteps.isEmpty());
    }

    @Override
    public void onFinished(Consumer<TaskFinishedReason> consumer) {
        this.onFinishedConsumer = consumer;
    }

    @Override
    public TaskStep getCurrentTaskStep() {
        return currentTaskStep;
    }

    public void onFinished() {
        if (onFinishedConsumer != null) {
            onFinishedConsumer.accept(getFinishedReason());
        }
    }

    public boolean isCancellable() {
        return cancellable;
    }

    public void setCancellable(boolean cancellable) {
        this.cancellable = cancellable;
    }

    private TaskFinishedReason getFinishedReason() {
        if (!isFinished()) {
            throw new RuntimeException("Attempted to get a finished reason on a task that is still running.");
        }

        if (hasFailed) {
            return TaskFinishedReason.FAILED;
        }

        if (isCancelled) {
            return TaskFinishedReason.CANCELLED;
        }

        return TaskFinishedReason.SUCCEED;
    }

    private void getNextStep() {
        this.isFirstTickForStep = true;
        this.currentTaskStep = taskSteps.poll();
    }
}
