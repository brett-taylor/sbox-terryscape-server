package com.terryscape.game.chat.dialogue.type;

import com.terryscape.game.interfaces.InterfaceManager;
import com.terryscape.net.Client;
import com.terryscape.net.OutgoingPacket;

public final class PlayerDialogueStep implements DialogueStep {

    private final String message;

    public PlayerDialogueStep(String message) {
        this.message = message;
    }

    @Override
    public void show(Client client, InterfaceManager interfaceManager) {
        interfaceManager.showInterface(client, "player_chat", stream -> {
            OutgoingPacket.writeString(stream, message);
        });
    }

    @Override
    public void close(Client client, InterfaceManager interfaceManager) {
        interfaceManager.closeInterface(client, "player_chat");
    }
}
