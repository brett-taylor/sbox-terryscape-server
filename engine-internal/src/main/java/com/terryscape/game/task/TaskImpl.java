package com.terryscape.game.task;

import com.terryscape.game.task.step.Step;

import java.util.Queue;

public class TaskImpl implements Task {

    private final Queue<Step> steps;

    private Step currentStep;

    private boolean isCancelled;

    private boolean isFirstTickForStep;

    private Runnable onFinishedRunnable;

    public TaskImpl(Queue<Step> steps) {
        this.steps = steps;

        getNextStep();
    }

    public void tick() {
        if (isFirstTickForStep) {
            currentStep.firstTick();
            isFirstTickForStep = false;
        }

        currentStep.tick();

        if (currentStep.isFinished()) {
            getNextStep();
        }
    }

    public boolean isFinished() {
        return currentStep == null && steps.isEmpty();
    }

    @Override
    public void cancel() {
        isCancelled = true;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void onFinished(Runnable runnable) {
        this.onFinishedRunnable = runnable;
    }

    public void onFinished() {
        if (onFinishedRunnable != null) {
            onFinishedRunnable.run();
        }
    }

    private void getNextStep() {
        this.isFirstTickForStep = true;
        this.currentStep = steps.poll();
    }
}
