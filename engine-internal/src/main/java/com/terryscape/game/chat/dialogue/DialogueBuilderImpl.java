package com.terryscape.game.chat.dialogue;

import com.terryscape.cache.item.ItemDefinition;
import com.terryscape.cache.npc.NpcDefinition;
import com.terryscape.game.chat.dialogue.type.*;

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

    @Override
    public DialogueBuilder item(ItemDefinition item, String message) {
        steps.add(new ItemDialogueStep(item, message));
        return this;
    }

    @Override
    public DialogueBuilder blank(String message) {
        steps.add(new BlankDialogueStep(message));
        return this;
    }

    public Queue<DialogueStep> buildSteps() {
        return new LinkedBlockingQueue<>(steps);
    }

}
