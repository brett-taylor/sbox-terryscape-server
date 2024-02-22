package com.terryscape.game.chat.dialogue.type;

import com.terryscape.cache.npc.NpcDefinition;
import com.terryscape.game.interfaces.InterfaceManager;
import com.terryscape.net.Client;
import com.terryscape.net.OutgoingPacket;

public final class NpcDialogueStep implements DialogueStep {

    private final String npcId;

    private final String message;

    public NpcDialogueStep(NpcDefinition npc, String message) {
        this.npcId = npc.getId();
        this.message = message;
    }

    @Override
    public void show(Client client, InterfaceManager interfaceManager) {
        interfaceManager.showInterface(client, "npc_chat", stream -> {
            OutgoingPacket.writeString(stream, npcId);
            OutgoingPacket.writeString(stream, message);
        });
    }

    @Override
    public void close(Client client, InterfaceManager interfaceManager) {
        interfaceManager.closeInterface(client, "npc_chat");
    }
}
