package com.terryscape.game.interfaces;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.net.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Singleton
public class InterfaceActionDispatcher {

    private static final Logger LOGGER = LogManager.getLogger(InterfaceActionDispatcher.class);

    private final Map<String, InterfaceActionHandler> interfaceActionHandlers;

    @Inject
    public InterfaceActionDispatcher(Set<InterfaceActionHandler> handlers) {
        interfaceActionHandlers = new HashMap<>();

        handlers.forEach(this::registerInterfaceActionHandler);
        LOGGER.info("Registered action handlers for {} interfaces.", interfaceActionHandlers.size());
    }

    public void dispatchInterfaceAction(Client client, String interfaceId, String interfaceAction, ByteBuffer packet) {
        var handler = interfaceActionHandlers.get(interfaceId);
        if (handler == null) {
            LOGGER.error("No interface action handler found for interface with id {}.", interfaceId);
            return;
        }

        handler.handleAction(client, interfaceId, interfaceAction, packet);
    }

    private void registerInterfaceActionHandler(InterfaceActionHandler handler) {
        for (var interfaceId : handler.getInterfaceId()) {
            if (interfaceActionHandlers.containsKey(interfaceId)) {
                throw new RuntimeException("A InterfaceActionHandler can't be registered to interface %s as it already has one".formatted(interfaceId));
            }

            interfaceActionHandlers.put(interfaceId, handler);
        }
    }
}
