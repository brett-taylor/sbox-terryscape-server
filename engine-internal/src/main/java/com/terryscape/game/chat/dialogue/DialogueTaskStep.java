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
    public void firstTick() {
        super.firstTick();

        proceed();
    }

    @Override
    public void cancel() {
        super.cancel();

        closeInterface();
        steps.clear();
    }

    public void proceed() {
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
