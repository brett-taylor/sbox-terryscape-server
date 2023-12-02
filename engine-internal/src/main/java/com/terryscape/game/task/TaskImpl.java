package com.terryscape.game.task;

import com.terryscape.game.task.step.Step;

import java.util.Queue;

public class TaskImpl implements Task {

    private final Queue<Step> steps;

    private Step currentStep;

    public TaskImpl(Queue<Step> steps) {
        this.steps = steps;

        this.currentStep = this.steps.poll();
    }

    public void tick() {
        this.currentStep.tick();

        if (this.currentStep.isFinished()) {
            this.currentStep = steps.poll();
        }
    }

    public boolean isFinished() {
        return currentStep == null && steps.isEmpty();
    }
}
