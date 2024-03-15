package com.terryscape.cache.npc;

public class NpcCombatBonusesDefinitionImpl implements NpcCombatBonusesDefinition {

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

    @Override
    public float getOffensiveStab() {
        return offensiveStab;
    }

    public NpcCombatBonusesDefinitionImpl setOffensiveStab(float offensiveStab) {
        this.offensiveStab = offensiveStab;
        return this;
    }

    @Override
    public float getOffensiveSlash() {
        return offensiveSlash;
    }

    public NpcCombatBonusesDefinitionImpl setOffensiveSlash(float offensiveSlash) {
        this.offensiveSlash = offensiveSlash;
        return this;
    }

    @Override
    public float getOffensiveAir() {
        return offensiveAir;
    }

    public NpcCombatBonusesDefinitionImpl setOffensiveAir(float offensiveAir) {
        this.offensiveAir = offensiveAir;
        return this;
    }

    @Override
    public float getOffensiveFire() {
        return offensiveFire;
    }

    public NpcCombatBonusesDefinitionImpl setOffensiveFire(float offensiveFire) {
        this.offensiveFire = offensiveFire;
        return this;
    }

    @Override
    public float getOffensiveArrow() {
        return offensiveArrow;
    }

    public NpcCombatBonusesDefinitionImpl setOffensiveArrow(float offensiveArrow) {
        this.offensiveArrow = offensiveArrow;
        return this;
    }

    @Override
    public float getDefensiveStab() {
        return defensiveStab;
    }

    public NpcCombatBonusesDefinitionImpl setDefensiveStab(float defensiveStab) {
        this.defensiveStab = defensiveStab;
        return this;
    }

    @Override
    public float getDefensiveSlash() {
        return defensiveSlash;
    }

    public NpcCombatBonusesDefinitionImpl setDefensiveSlash(float defensiveSlash) {
        this.defensiveSlash = defensiveSlash;
        return this;
    }

    @Override
    public float getDefensiveAir() {
        return defensiveAir;
    }

    public NpcCombatBonusesDefinitionImpl setDefensiveAir(float defensiveAir) {
        this.defensiveAir = defensiveAir;
        return this;
    }

    @Override
    public float getDefensiveFire() {
        return defensiveFire;
    }

    public NpcCombatBonusesDefinitionImpl setDefensiveFire(float defensiveFire) {
        this.defensiveFire = defensiveFire;
        return this;
    }

    @Override
    public float getDefensiveArrow() {
        return defensiveArrow;
    }

    public NpcCombatBonusesDefinitionImpl setDefensiveArrow(float defensiveArrow) {
        this.defensiveArrow = defensiveArrow;
        return this;
    }

    @Override
    public float getStrengthMelee() {
        return strengthMelee;
    }

    public NpcCombatBonusesDefinitionImpl setStrengthMelee(float strengthMelee) {
        this.strengthMelee = strengthMelee;
        return this;
    }

    @Override
    public float getStrengthMagic() {
        return strengthMagic;
    }

    public NpcCombatBonusesDefinitionImpl setStrengthMagic(float strengthMagic) {
        this.strengthMagic = strengthMagic;
        return this;
    }

    @Override
    public float getStrengthRange() {
        return strengthRange;
    }

    public NpcCombatBonusesDefinitionImpl setStrengthRange(float strengthRange) {
        this.strengthRange = strengthRange;
        return this;
    }
}
