package com.terryscape.game.npc;

import com.terryscape.cache.npc.NpcDefinition;
import com.terryscape.net.Client;

import java.util.Set;

public interface NpcInteractionHandler {

    Set<NpcDefinition> getNpcs();

    void invoke(Client client, NpcComponent npcComponent);
}
