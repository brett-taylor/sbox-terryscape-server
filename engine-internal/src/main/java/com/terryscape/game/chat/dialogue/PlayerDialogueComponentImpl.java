package com.terryscape.game.chat.dialogue;

import com.terryscape.entity.Entity;
import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.game.chat.dialogue.type.DialogueStep;
import com.terryscape.game.interfaces.InterfaceManager;
import com.terryscape.game.player.PlayerComponent;

import java.util.Queue;

public class PlayerDialogueComponentImpl extends BaseEntityComponent implements PlayerDialogueComponent {

    private final InterfaceManager interfaceManager;

    private Queue<DialogueStep> steps;

    private DialogueStep currentStep;

    public PlayerDialogueComponentImpl(Entity entity, InterfaceManager interfaceManager) {
        super(entity);

        this.interfaceManager = interfaceManager;
    }

    @Override
    public DialogueBuilder builder() {
        return new DialogueBuilderImpl();
    }

    @Override
    public void start(DialogueBuilder dialogue) {
        steps = ((DialogueBuilderImpl) dialogue).buildSteps();
        showNextStep();
    }

    public void proceed() {
        showNextStep();
    }

    private void showNextStep() {
        var client = getEntity().getComponentOrThrow(PlayerComponent.class).getClient();

        if (currentStep != null) {
            currentStep.close(client, interfaceManager);
            currentStep = null;
        }

        if (steps == null || steps.isEmpty()) {
            steps = null;
            return;
        }

        currentStep = steps.poll();
        currentStep.show(client, interfaceManager);
    }
}
