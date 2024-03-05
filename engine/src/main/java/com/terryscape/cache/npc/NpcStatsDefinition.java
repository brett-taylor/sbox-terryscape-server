package com.terryscape.cache.npc;

public interface NpcStatsDefinition {

    int getHealth();

    NpcCombatSkillsDefinition getCombatSkillsDefinition();

    NpcCombatBonusesDefinition getCombatBonusesDefinition();

}
