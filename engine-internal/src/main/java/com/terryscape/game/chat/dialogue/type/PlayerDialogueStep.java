package com.terryscape.game.chat.dialogue.type;

import com.terryscape.game.interfaces.InterfaceManager;
import com.terryscape.net.Client;

public final class PlayerDialogueStep implements DialogueStep {

    private final String message;

    public PlayerDialogueStep(String message) {
        this.message = message;
    }

    @Override
    public void show(Client client, InterfaceManager interfaceManager) {
        interfaceManager.showInterface(client, "player_chat");
    }

    @Override
    public void close(Client client, InterfaceManager interfaceManager) {
        interfaceManager.closeInterface(client, "player_chat");
    }
}
