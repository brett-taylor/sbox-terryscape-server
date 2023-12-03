package com.terryscape.game.task;

import com.terryscape.game.task.step.Step;

import java.util.Queue;
import java.util.function.Consumer;

public class TaskImpl implements Task {

    private final Queue<Step> steps;

    private Step currentStep;

    private boolean isCancelled;

    private boolean isFirstTickForStep;

    private boolean hasFailed;

    private boolean cancellable;

    private Consumer<TaskFinishedReason> onFinishedConsumer;

    public TaskImpl(Queue<Step> steps) {
        this.steps = steps;

        getNextStep();
    }

    public void tick() {
        if (isFinished()) {
            return;
        }

        if (isFirstTickForStep) {
            currentStep.firstTick();
            isFirstTickForStep = false;
        }

        currentStep.tick();

        if (currentStep.hasFailed()) {
            hasFailed = true;
            currentStep.cancel();
            return;
        }

        if (currentStep.isFinished()) {
            getNextStep();
        }
    }

    @Override
    public void cancel() {
        if (currentStep != null) {
            currentStep.cancel();
        }

        isCancelled = true;
    }

    @Override
    public boolean isFinished() {
        return hasFailed || isCancelled || (currentStep == null && steps.isEmpty());
    }

    @Override
    public void onFinished(Consumer<TaskFinishedReason> consumer) {
        this.onFinishedConsumer = consumer;
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
        this.currentStep = steps.poll();
    }
}
