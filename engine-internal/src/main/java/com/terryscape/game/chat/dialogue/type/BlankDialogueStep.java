package com.terryscape.game.chat.dialogue.type;

import com.terryscape.game.interfaces.InterfaceManager;
import com.terryscape.net.Client;
import com.terryscape.net.OutgoingPacket;

public final class BlankDialogueStep implements DialogueStep {

    private final String message;

    public BlankDialogueStep(String message) {
        this.message = message;
    }

    @Override
    public void show(Client client, InterfaceManager interfaceManager) {
        interfaceManager.showInterface(client, "blank_chat", stream -> {
            OutgoingPacket.writeString(stream, message);
        });
    }

    @Override
    public void close(Client client, InterfaceManager interfaceManager) {
        interfaceManager.closeInterface(client, "blank_chat");
    }
}
