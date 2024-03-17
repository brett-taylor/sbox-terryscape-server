package com.terryscape.game.item;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.item.ItemDefinition;
import com.terryscape.net.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Singleton
public class ItemInteractionDispatcher {

    private static final Logger LOGGER = LogManager.getLogger(ItemInteractionDispatcher.class);

    private final Map<ItemDefinition, ItemInteractionHandler> itemHandlers;

    @Inject
    public ItemInteractionDispatcher(Set<ItemInteractionHandler> handlers) {
        itemHandlers = new HashMap<>();

        handlers.forEach(this::registerSingleItemInteractionHandler);
        LOGGER.info("Registered interaction handlers for {} items.", itemHandlers.size());
    }

    public void dispatchItemInteraction(Client client, int inventorySlot) {
        var playerInventory = client.getPlayer().orElseThrow().getInventory();
        var item = playerInventory.getItemAt(inventorySlot).orElseThrow();

        var handler = itemHandlers.get(item.getItemDefinition());
        if (handler == null) {
            LOGGER.error("No item interaction handler found for item {}.", item.getItemDefinition());
            return;
        }

        handler.invoke(client, item.getItemDefinition(), playerInventory, inventorySlot);
    }

    private void registerSingleItemInteractionHandler(ItemInteractionHandler handler) {
        for (var item : handler.getItems()) {
            if (itemHandlers.containsKey(item)) {
                throw new RuntimeException("A ItemInteractionHandler can't be registered to item %s as it already has one".formatted(item));
            }

            itemHandlers.put(item, handler);
        }
    }
}
