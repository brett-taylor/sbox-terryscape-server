package com.terryscape.game.chat.dialogue;

import com.terryscape.game.chat.dialogue.type.DialogueStep;
import com.terryscape.game.interfaces.InterfaceManager;
import com.terryscape.game.task.step.TaskStep;
import com.terryscape.net.Client;

import java.util.Queue;

public class DialogueTaskStep extends TaskStep {

    private final InterfaceManager interfaceManager;

    private final Client client;

    private final Queue<DialogueStep> steps;

    private DialogueStep currentStep;

    private boolean shouldProceedOnNextTick;

    public DialogueTaskStep(InterfaceManager interfaceManager, Client client, DialogueBuilder dialogueBuilder) {
        this.interfaceManager = interfaceManager;
        this.client = client;
        steps = ((DialogueBuilderImpl) dialogueBuilder).buildSteps();
    }

    @Override
    public boolean isFinished() {
        return currentStep == null && steps.isEmpty();
    }

    @Override
    public void onBecameCurrentTaskStep() {
        super.onBecameCurrentTaskStep();

        closeCurrentStepAndShowNextStep();
    }

    @Override
    public void tick() {
        super.tick();

        if (shouldProceedOnNextTick) {
            closeCurrentStepAndShowNextStep();
            shouldProceedOnNextTick = false;
        }
    }

    @Override
    public void cancel() {
        super.cancel();

        closeInterface();
        steps.clear();
    }

    public void proceed() {
        shouldProceedOnNextTick = true;
    }

    private void closeCurrentStepAndShowNextStep() {
        closeInterface();

        if (steps.isEmpty()) {
            return;
        }

        currentStep = steps.poll();
        currentStep.show(client, interfaceManager);
    }

    private void closeInterface() {
        if (currentStep != null) {
            currentStep.close(client, interfaceManager);
            currentStep = null;
        }
    }
}
