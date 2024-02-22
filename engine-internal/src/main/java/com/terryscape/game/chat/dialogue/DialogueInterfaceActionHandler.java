package com.terryscape.game.chat.dialogue;

import com.google.inject.Singleton;
import com.terryscape.game.interfaces.InterfaceActionHandler;
import com.terryscape.game.task.TaskComponent;
import com.terryscape.net.Client;

import java.nio.ByteBuffer;
import java.util.Set;

@Singleton
public class DialogueInterfaceActionHandler implements InterfaceActionHandler {

    @Override
    public Set<String> getInterfaceId() {
        return Set.of("player_chat", "npc_chat");
    }

    @Override
    public void handleAction(Client client, String interfaceId, String interfaceAction, ByteBuffer packet) {
        var taskComponent = client.getPlayer().orElseThrow().getEntity().getComponentOrThrow(TaskComponent.class);

        if (taskComponent.hasPrimaryTask()) {
            var currentTaskStep = taskComponent.getPrimaryTask().orElseThrow().getCurrentTaskStep();

            if (currentTaskStep instanceof DialogueTaskStep dialogueTaskStep) {
                dialogueTaskStep.proceed();
            }
        }
    }
}
