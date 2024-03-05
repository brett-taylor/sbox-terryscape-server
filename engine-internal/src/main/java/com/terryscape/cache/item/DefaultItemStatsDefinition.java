package com.terryscape.cache.item;

public class DefaultItemStatsDefinition implements ItemStatsDefinition {

    @Override
    public float getWeight() {
        return 0;
    }

    @Override
    public float getOffensiveStab() {
        return 0;
    }

    @Override
    public float getOffensiveSlash() {
        return 0;
    }

    @Override
    public float getDefensiveStab() {
        return 0;
    }

    @Override
    public float getDefensiveSlash() {
        return 0;
    }

    @Override
    public String toString() {
        return "DefaultItemStats";
    }
}
