package com.terryscape.game.loottable;

import com.terryscape.cache.item.ItemDefinition;

public class LootTableItem {

    public static LootTableItem one(ItemDefinition itemDefinition) {
        return new LootTableItem(itemDefinition, 1, 1);
    }

    public static LootTableItem randomAmount(ItemDefinition itemDefinition, int min, int max) {
        if (min <= 0) {
            throw new RuntimeException("LootTableItem::min can not be 0 or less");
        }

        if (min >= max) {
            throw new RuntimeException("LootTableItem::min can not be equal to or greater than LootTableItem::max");
        }

        return new LootTableItem(itemDefinition, min, max);
    }

    private final ItemDefinition itemDefinition;

    private final int min;

    private final int max;

    private LootTableItem(ItemDefinition itemDefinition, int min, int max) {
        this.itemDefinition = itemDefinition;
        this.min = min;
        this.max = max;
    }

    public ItemDefinition getItemDefinition() {
        return itemDefinition;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

}
