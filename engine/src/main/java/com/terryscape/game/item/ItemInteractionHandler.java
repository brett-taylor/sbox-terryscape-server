package com.terryscape.game.item;

import com.terryscape.cache.item.ItemDefinition;
import com.terryscape.net.Client;

import java.util.Set;

public interface ItemInteractionHandler {

    Set<ItemDefinition> getItems();

    void invoke(Client client, ItemDefinition itemDefinition, FixedSizeItemContainer playerInventory, int inventorySlot);

}
