package com.terryscape.cache.item;

public class ItemStatsDefinitionImpl implements ItemStatsDefinition {

    private String id;

    private float weight;

    private float offensiveStab;

    private float offensiveSlash;

    private float defensiveStab;

    private float defensiveSlash;

    @Override
    public String getId() {
        return id;
    }

    public ItemStatsDefinitionImpl setId(String id) {
        this.id = id;
        return this;
    }

    @Override
    public float getWeight() {
        return weight;
    }

    public ItemStatsDefinitionImpl setWeight(float weight) {
        this.weight = weight;
        return this;
    }

    @Override
    public float getOffensiveStab() {
        return offensiveStab;
    }

    public ItemStatsDefinitionImpl setOffensiveStab(float offensiveStab) {
        this.offensiveStab = offensiveStab;
        return this;
    }

    @Override
    public float getOffensiveSlash() {
        return offensiveSlash;
    }

    public ItemStatsDefinitionImpl setOffensiveSlash(float offensiveSlash) {
        this.offensiveSlash = offensiveSlash;
        return this;
    }

    @Override
    public float getDefensiveStab() {
        return defensiveStab;
    }

    public ItemStatsDefinitionImpl setDefensiveStab(float defensiveStab) {
        this.defensiveStab = defensiveStab;
        return this;
    }

    @Override
    public float getDefensiveSlash() {
        return defensiveSlash;
    }

    public ItemStatsDefinitionImpl setDefensiveSlash(float defensiveSlash) {
        this.defensiveSlash = defensiveSlash;
        return this;
    }
}
