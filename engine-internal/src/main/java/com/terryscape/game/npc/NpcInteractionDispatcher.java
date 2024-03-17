package com.terryscape.game.npc;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.npc.NpcDefinition;
import com.terryscape.net.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Singleton
public class NpcInteractionDispatcher {

    private static final Logger LOGGER = LogManager.getLogger(NpcInteractionDispatcher.class);

    private final Map<NpcDefinition, NpcInteractionHandler> npcHandlers;

    @Inject
    public NpcInteractionDispatcher(Set<NpcInteractionHandler> handlers) {
        npcHandlers = new HashMap<>();

        handlers.forEach(this::registerSingleNpcInteractionHandler);
        LOGGER.info("Registered interaction handlers for {} npcs.", npcHandlers.size());
    }

    public void dispatchNpcInteraction(Client client, NpcComponent npcComponent) {
        var handler = npcHandlers.get(npcComponent.getNpcDefinition());
        if (handler == null) {
            LOGGER.error("No npc interaction handler found for npc {}.", npcComponent.getNpcDefinition());
            return;
        }

        handler.invoke(client, npcComponent);
    }


    private void registerSingleNpcInteractionHandler(NpcInteractionHandler handler) {
        for (var npc : handler.getNpcs()) {
            if (npcHandlers.containsKey(npc)) {
                throw new RuntimeException("A NpcInteractionHandler can't be registered to npc %s as it already has one".formatted(npc));
            }

            npcHandlers.put(npc, handler);
        }
    }
}
