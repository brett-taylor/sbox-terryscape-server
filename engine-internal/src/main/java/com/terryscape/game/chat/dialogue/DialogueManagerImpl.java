package com.terryscape.game.chat.dialogue;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.game.interfaces.InterfaceManager;
import com.terryscape.game.task.step.TaskStep;
import com.terryscape.net.Client;

@Singleton
public class DialogueManagerImpl implements DialogueManager {

    private final InterfaceManager interfaceManager;

    @Inject
    public DialogueManagerImpl(InterfaceManager interfaceManager) {
        this.interfaceManager = interfaceManager;
    }

    @Override
    public DialogueBuilder builder() {
        return new DialogueBuilderImpl();
    }

    @Override
    public TaskStep createViewDialogueTaskStep(Client client, DialogueBuilder dialogue) {
        return new ViewDialogueTaskStep(interfaceManager, client, dialogue);
    }
}
