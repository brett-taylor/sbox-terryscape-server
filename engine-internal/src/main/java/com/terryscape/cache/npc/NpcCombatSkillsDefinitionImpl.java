package com.terryscape.cache.npc;

public class NpcCombatSkillsDefinitionImpl implements NpcCombatSkillsDefinition {

    private int accuracy;

    private int defence;

    private int strength;

    @Override
    public int getAccuracy() {
        return accuracy;
    }

    public NpcCombatSkillsDefinitionImpl setAccuracy(int accuracy) {
        this.accuracy = accuracy;
        return this;
    }

    @Override
    public int getDefence() {
        return defence;
    }

    public NpcCombatSkillsDefinitionImpl setDefence(int defence) {
        this.defence = defence;
        return this;
    }

    @Override
    public int getStrength() {
        return strength;
    }

    public NpcCombatSkillsDefinitionImpl setStrength(int strength) {
        this.strength = strength;
        return this;
    }
}
