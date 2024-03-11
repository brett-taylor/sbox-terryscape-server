package com.terryscape.cache.npc;

public class NpcCombatSkillsDefinitionImpl implements NpcCombatSkillsDefinition {

    private int attack;

    private int defence;

    private int strength;

    private int magic;

    @Override
    public int getAttack() {
        return attack;
    }

    public NpcCombatSkillsDefinitionImpl setAttack(int attack) {
        this.attack = attack;
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

    @Override
    public int getMagic() {
        return magic;
    }

    public NpcCombatSkillsDefinitionImpl setMagic(int magic) {
        this.magic = magic;
        return this;
    }
}
