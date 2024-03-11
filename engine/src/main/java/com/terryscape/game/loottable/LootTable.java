package com.terryscape.game.loottable;

import java.util.List;

public class LootTable {

    private final List<LootTableItem> guaranteedDrops;

    private final List<LootTableItem> optionalDrops;

    public LootTable(List<LootTableItem> guaranteedDrops, List<LootTableItem> optionalDrops) {
        this.guaranteedDrops = guaranteedDrops;
        this.optionalDrops = optionalDrops;
    }

    public List<LootTableItem> getGuaranteedDrops() {
        return guaranteedDrops;
    }

    public List<LootTableItem> getOptionalDrops() {
        return optionalDrops;
    }
}
