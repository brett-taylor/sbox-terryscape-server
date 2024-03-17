package com.terryscape.cache.item;

public class ItemStatsDefinitionImpl implements ItemStatsDefinition {

    private String id;

    private float weight;

    private float offensiveStab;

    private float offensiveSlash;

    private float offensiveAir;

    private float offensiveFire;

    private float offensiveArrow;

    private float defensiveStab;

    private float defensiveSlash;

    private float defensiveAir;

    private float defensiveFire;

    private float defensiveArrow;

    private float strengthMelee;

    private float strengthMagic;

    private float strengthRange;

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
    public float getOffensiveAir() {
        return offensiveAir;
    }

    public ItemStatsDefinitionImpl setOffensiveAir(float offensiveAir) {
        this.offensiveAir = offensiveAir;
        return this;
    }

    @Override
    public float getOffensiveFire() {
        return offensiveFire;
    }

    public ItemStatsDefinitionImpl setOffensiveFire(float offensiveFire) {
        this.offensiveFire = offensiveFire;
        return this;
    }

    @Override
    public float getOffensiveArrow() {
        return offensiveArrow;
    }

    public ItemStatsDefinitionImpl setOffensiveArrow(float offensiveArrow) {
        this.offensiveArrow = offensiveArrow;
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

    @Override
    public float getDefensiveAir() {
        return defensiveAir;
    }

    public ItemStatsDefinitionImpl setDefensiveAir(float defensiveAir) {
        this.defensiveAir = defensiveAir;
        return this;
    }

    @Override
    public float getDefensiveFire() {
        return defensiveFire;
    }

    public ItemStatsDefinitionImpl setDefensiveFire(float defensiveFire) {
        this.defensiveFire = defensiveFire;
        return this;
    }

    @Override
    public float getDefensiveArrow() {
        return defensiveArrow;
    }

    public ItemStatsDefinitionImpl setDefensiveArrow(float defensiveArrow) {
        this.defensiveArrow = defensiveArrow;
        return this;
    }

    @Override
    public float getStrengthMelee() {
        return strengthMelee;
    }

    public ItemStatsDefinitionImpl setStrengthMelee(float strengthMelee) {
        this.strengthMelee = strengthMelee;
        return this;
    }

    @Override
    public float getStrengthMagic() {
        return strengthMagic;
    }

    public ItemStatsDefinitionImpl setStrengthMagic(float strengthMagic) {
        this.strengthMagic = strengthMagic;
        return this;
    }

    @Override
    public float getStrengthRange() {
        return strengthRange;
    }

    public ItemStatsDefinitionImpl setStrengthRange(float strengthRange) {
        this.strengthRange = strengthRange;
        return this;
    }

    @Override
    public String toString() {
        return "ItemStatsDefinition(id=%s)".formatted(id);
    }
}
