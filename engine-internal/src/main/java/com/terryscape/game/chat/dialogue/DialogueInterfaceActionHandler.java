package com.terryscape.game.chat.dialogue;

import com.google.inject.Singleton;
import com.terryscape.game.interfaces.InterfaceActionHandler;
import com.terryscape.game.player.PlayerComponent;
import com.terryscape.game.task.TaskComponent;
import com.terryscape.net.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.ByteBuffer;
import java.util.Set;

@Singleton
public class DialogueInterfaceActionHandler implements InterfaceActionHandler {

    private static final Logger LOGGER = LogManager.getLogger(DialogueInterfaceActionHandler.class);

    @Override
    public Set<String> getInterfaceId() {
        return Set.of("player_chat", "npc_chat", "item_chat");
    }

    @Override
    public void handleAction(Client client, String interfaceId, String interfaceAction, ByteBuffer packet) {
        var taskComponent = client.getPlayer().orElseThrow().getEntity().getComponentOrThrow(TaskComponent.class);
        if (!taskComponent.hasPrimaryTask()) {
            LOGGER.warn(
                "{} triggered the dialogue interface action handler without a primary task",
                client.getPlayer().map(PlayerComponent::getUsername).orElse("")
            );
            return;
        }

        var currentTaskStep = taskComponent.getPrimaryTask().orElseThrow().getCurrentTaskStep();
        if (!(currentTaskStep instanceof ViewDialogueTaskStep viewDialogueTaskStep)) {
            LOGGER.warn(
                "{} triggered the dialogue interface action handler without their primary task being ViewDialogue",
                client.getPlayer().map(PlayerComponent::getUsername).orElse("")
            );

            return;
        }

        viewDialogueTaskStep.proceed();
    }
}
