package com.terryscape.game.worldobject;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.object.ObjectDefinition;
import com.terryscape.cache.world.WorldObjectDefinition;
import com.terryscape.net.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Singleton
public class WorldObjectInteractionDispatcher {

    private static final Logger LOGGER = LogManager.getLogger(WorldObjectInteractionDispatcher.class);

    private final Map<ObjectDefinition, WorldObjectInteractionHandler> worldObjectHandlers;

    @Inject
    public WorldObjectInteractionDispatcher(Set<WorldObjectInteractionHandler> handlers) {
        worldObjectHandlers = new HashMap<>();

        handlers.forEach(this::registerSingleWorldObjectInteractionHandler);
        LOGGER.info("Registered interaction handlers for {} world objects.", worldObjectHandlers.size());
    }

    public void dispatchWorldObjectInteraction(Client client, WorldObjectDefinition worldObjectDefinition) {
        var handler = worldObjectHandlers.get(worldObjectDefinition.getObjectDefinition());
        if (handler == null) {
            LOGGER.error("No world object interaction handler found for object {}.", worldObjectDefinition.getObjectDefinition());
            return;
        }

        handler.invoke(client, worldObjectDefinition);
    }


    private void registerSingleWorldObjectInteractionHandler(WorldObjectInteractionHandler handler) {
        for (var object : handler.getObjects()) {
            if (worldObjectHandlers.containsKey(object)) {
                throw new RuntimeException("A WorldObjectInteractionHandler can't be registered to object %s as it already has one".formatted(object));
            }

            worldObjectHandlers.put(object, handler);
        }
    }
}
