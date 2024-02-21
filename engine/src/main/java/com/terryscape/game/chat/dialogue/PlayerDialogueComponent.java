package com.terryscape.game.chat.dialogue;

import com.terryscape.entity.component.EntityComponent;

public interface PlayerDialogueComponent extends EntityComponent {

    DialogueBuilder builder();

    void start(DialogueBuilder dialogue);
}
