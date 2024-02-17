package com.terryscape.game.worldobject;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.world.WorldObjectDefinition;
import com.terryscape.net.Client;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Singleton
public class WorldObjectInteractionManager {

    private final Map<String, WorldObjectInteractionHandler> worldObjectHandlers;

    @Inject
    public WorldObjectInteractionManager(Set<WorldObjectInteractionHandler> handlers) {
        worldObjectHandlers = new HashMap<>();

        handlers.forEach(this::registerSingleWorldObjectInteractionHandler);
    }

    public void dispatchWorldObjectInteraction(Client client, WorldObjectDefinition worldObjectDefinition) {
        var handler = worldObjectHandlers.get(worldObjectDefinition.getObjectDefinition().getId());
        handler.invoke(client, worldObjectDefinition);
    }


    private void registerSingleWorldObjectInteractionHandler(WorldObjectInteractionHandler handler) {
        for (var objectId : handler.getObjectId()) {
            if (worldObjectHandlers.containsKey(objectId)) {
                throw new RuntimeException("A handler can't be registered to object %s as it already has one".formatted(handler.getObjectId()));
            }

            worldObjectHandlers.put(objectId, handler);
        }
    }
}
