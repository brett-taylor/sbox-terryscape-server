package com.terryscape.game.chat.dialogue;

import com.terryscape.cache.npc.NpcDefinition;

public interface DialogueBuilder {

    DialogueBuilder npc(NpcDefinition npc, String message);

    DialogueBuilder player(String message);

}
