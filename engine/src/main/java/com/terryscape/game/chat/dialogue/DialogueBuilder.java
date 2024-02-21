package com.terryscape.game.chat.dialogue;

public interface DialogueBuilder {

    DialogueBuilder npc(String name, String message);

    DialogueBuilder player(String message);

}
