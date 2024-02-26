package com.terryscape.game.chat.dialogue;

import com.terryscape.entity.Entity;
import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.game.interfaces.InterfaceManager;
import com.terryscape.game.player.PlayerComponent;
import com.terryscape.game.task.step.TaskStep;
import com.terryscape.net.Client;

public class PlayerDialogueComponentImpl extends BaseEntityComponent implements PlayerDialogueComponent {

    private final InterfaceManager interfaceManager;

    private Client client;

    // TODO: This should just be a DialogueManager? I don't see why it has to be connected to the player

    public PlayerDialogueComponentImpl(Entity entity, InterfaceManager interfaceManager) {
        super(entity);

        this.interfaceManager = interfaceManager;
    }

    @Override
    public void onRegistered() {
        super.onRegistered();

        client = getEntity().getComponentOrThrow(PlayerComponent.class).getClient();
    }

    @Override
    public DialogueBuilder builder() {
        return new DialogueBuilderImpl();
    }

    @Override
    public TaskStep createDialogueTaskStep(DialogueBuilder dialogue) {
        return new DialogueTaskStep(interfaceManager, client, dialogue);
    }
}
