package com.terryscape.game.shop;

import com.terryscape.cache.item.ItemDefinition;

public class ShopItem {

    private final ItemDefinition itemDefinition;

    private final int price;

    public ShopItem(ItemDefinition itemDefinition, int price) {
        this.itemDefinition = itemDefinition;
        this.price = price;
    }

    public ItemDefinition getItemDefinition() {
        return itemDefinition;
    }

    public int getPrice() {
        return price;
    }
}
