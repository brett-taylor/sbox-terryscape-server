package com.terryscape.cache.npc;

public class DefaultNpcStatsDefinitionImpl implements NpcStatsDefinition {

    private final NpcCombatSkillsDefinition npcCombatSkillsDefinition;

    private final NpcCombatBonusesDefinition npcCombatBonusesDefinition;

    public DefaultNpcStatsDefinitionImpl() {
        npcCombatBonusesDefinition = new DefaultCombatBonuses();
        npcCombatSkillsDefinition = new DefaultCombatSkills();
    }

    @Override
    public int getHealth() {
        return 100;
    }

    @Override
    public NpcCombatSkillsDefinition getCombatSkillsDefinition() {
        return npcCombatSkillsDefinition;
    }

    @Override
    public NpcCombatBonusesDefinition getCombatBonusesDefinition() {
        return npcCombatBonusesDefinition;
    }

    @Override
    public String toString() {
        return "DefaultNpcStats";
    }

    private static class DefaultCombatSkills implements NpcCombatSkillsDefinition {
        @Override
        public int getAccuracy() {
            return 1;
        }

        @Override
        public int getDefence() {
            return 1;
        }

        @Override
        public int getStrength() {
            return 1;
        }
    }

    private static class DefaultCombatBonuses implements NpcCombatBonusesDefinition {
        @Override
        public float getOffensiveStab() {
            return 1;
        }

        @Override
        public float getOffensiveSlash() {
            return 1;
        }

        @Override
        public float getDefensiveStab() {
            return 1;
        }

        @Override
        public float getDefensiveSlash() {
            return 1;
        }
    }
}
