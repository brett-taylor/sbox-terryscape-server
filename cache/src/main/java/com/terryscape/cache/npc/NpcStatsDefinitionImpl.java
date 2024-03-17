package com.terryscape.cache.npc;

public class NpcStatsDefinitionImpl implements NpcStatsDefinition {

    private String id;

    private int health;

    private NpcCombatSkillsDefinitionImpl npcCombatSkillsDefinition;

    private NpcCombatBonusesDefinitionImpl npcCombatBonusesDefinition;

    public String getId() {
        return id;
    }

    public NpcStatsDefinitionImpl setId(String id) {
        this.id = id;
        return this;
    }

    @Override
    public int getHealth() {
        return health;
    }

    public NpcStatsDefinitionImpl setHealth(int health) {
        this.health = health;
        return this;
    }

    @Override
    public NpcCombatSkillsDefinition getCombatSkillsDefinition() {
        return npcCombatSkillsDefinition;
    }

    public NpcStatsDefinitionImpl setNpcCombatSkillsDefinition(NpcCombatSkillsDefinitionImpl npcCombatSkillsDefinition) {
        this.npcCombatSkillsDefinition = npcCombatSkillsDefinition;
        return this;
    }

    @Override
    public NpcCombatBonusesDefinition getCombatBonusesDefinition() {
        return npcCombatBonusesDefinition;
    }

    public NpcStatsDefinitionImpl setNpcCombatBonusesDefinition(NpcCombatBonusesDefinitionImpl npcCombatBonusesDefinition) {
        this.npcCombatBonusesDefinition = npcCombatBonusesDefinition;
        return this;
    }

    @Override
    public String toString() {
        return "NpcStatsDefinition(id=%s)".formatted(id);
    }
}
