package com.terryscape.cache.npc;

public class NpcCombatBonusesDefinitionImpl implements NpcCombatBonusesDefinition {

    private float offensiveStab;

    private float offensiveSlash;

    private float defensiveStab;

    private float defensiveSlash;

    private float strengthMelee;

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
    public float getStrengthMelee() {
        return strengthMelee;
    }

    public NpcCombatBonusesDefinitionImpl setStrengthMelee(float strengthMelee) {
        this.strengthMelee = strengthMelee;
        return this;
    }
}
