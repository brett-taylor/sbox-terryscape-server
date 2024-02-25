package com.terryscape.game.item;

import com.terryscape.cache.item.ItemDefinition;

public class ItemContainerItem {

    private final ItemDefinition itemDefinition;

    private final int quantity;

    public ItemContainerItem(ItemDefinition itemDefinition, int quantity) {
        this.itemDefinition = itemDefinition;
        this.quantity = quantity;
    }

    public ItemDefinition getItemDefinition() {
        return itemDefinition;
    }

    public int getQuantity() {
        return quantity;
    }
}
