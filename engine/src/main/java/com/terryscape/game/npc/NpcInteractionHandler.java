package com.terryscape.game.npc;

import com.terryscape.net.Client;

import java.util.Set;

public interface NpcInteractionHandler {

    Set<String> getNpcIds();

    void invoke(Client client, NpcComponent npcComponent);
}
