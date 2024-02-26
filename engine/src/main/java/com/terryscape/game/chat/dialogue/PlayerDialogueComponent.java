package com.terryscape.game.chat.dialogue;

import com.terryscape.entity.component.EntityComponent;
import com.terryscape.game.task.step.TaskStep;

public interface PlayerDialogueComponent extends EntityComponent {

    DialogueBuilder builder();

    TaskStep createViewDialogueTaskStep(DialogueBuilder dialogue);
}
