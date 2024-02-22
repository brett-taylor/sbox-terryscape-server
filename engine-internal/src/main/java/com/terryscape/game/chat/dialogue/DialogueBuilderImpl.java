package com.terryscape.game.chat.dialogue;

import com.terryscape.cache.npc.NpcDefinition;
import com.terryscape.game.chat.dialogue.type.DialogueStep;
import com.terryscape.game.chat.dialogue.type.NpcDialogueStep;
import com.terryscape.game.chat.dialogue.type.PlayerDialogueStep;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class DialogueBuilderImpl implements DialogueBuilder {

    private final List<DialogueStep> steps = new ArrayList<>();

    @Override
    public DialogueBuilder npc(NpcDefinition npc, String message) {
        steps.add(new NpcDialogueStep(npc, message));
        return this;
    }

    @Override
    public DialogueBuilder player(String message) {
        steps.add(new PlayerDialogueStep(message));
        return this;
    }

    public Queue<DialogueStep> buildSteps() {
        return new LinkedBlockingQueue<>(steps);
    }

}
