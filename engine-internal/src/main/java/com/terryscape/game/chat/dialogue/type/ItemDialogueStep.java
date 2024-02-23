package com.terryscape.game.chat.dialogue.type;

import com.terryscape.cache.item.ItemDefinition;
import com.terryscape.cache.npc.NpcDefinition;
import com.terryscape.game.interfaces.InterfaceManager;
import com.terryscape.net.Client;
import com.terryscape.net.OutgoingPacket;

public final class ItemDialogueStep implements DialogueStep {

    private final String itemId;

    private final String message;

    public ItemDialogueStep(ItemDefinition item, String message) {
        this.itemId = item.getId();
        this.message = message;
    }

    @Override
    public void show(Client client, InterfaceManager interfaceManager) {
        interfaceManager.showInterface(client, "item_chat", stream -> {
            OutgoingPacket.writeString(stream, itemId);
            OutgoingPacket.writeString(stream, message);
        });
    }

    @Override
    public void close(Client client, InterfaceManager interfaceManager) {
        interfaceManager.closeInterface(client, "item_chat");
    }
}
