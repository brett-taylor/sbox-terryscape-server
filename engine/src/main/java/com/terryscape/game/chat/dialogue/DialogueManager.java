package com.terryscape.game.chat.dialogue;

import com.terryscape.game.task.step.TaskStep;
import com.terryscape.net.Client;

public interface DialogueManager {

    DialogueBuilder builder();

    TaskStep createViewDialogueTaskStep(Client client, DialogueBuilder dialogue);

}
