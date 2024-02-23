package com.terryscape.game.chat.dialogue.type;

import com.terryscape.game.interfaces.InterfaceManager;
import com.terryscape.net.Client;

public sealed interface DialogueStep permits PlayerDialogueStep, NpcDialogueStep, ItemDialogueStep {

    void show(Client client, InterfaceManager interfaceManager);

    void close(Client client, InterfaceManager interfaceManager);
}
