package com.terryscape.game.npc;

import com.terryscape.entity.Entity;
import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.game.combat.CombatSkillsProviderComponent;

public class NpcCombatSkillsProviderComponent extends BaseEntityComponent implements CombatSkillsProviderComponent {

    private final NpcComponent npcComponent;

    public NpcCombatSkillsProviderComponent(Entity entity, NpcComponent npcComponent) {
        super(entity);

        this.npcComponent = npcComponent;
    }

    @Override
    public int getAttack() {
        return npcComponent.getNpcDefinition().getStatsDefinition().getCombatSkillsDefinition().getAttack();
    }

    @Override
    public int getDefence() {
        return npcComponent.getNpcDefinition().getStatsDefinition().getCombatSkillsDefinition().getDefence();
    }

    @Override
    public int getStrength() {
        return npcComponent.getNpcDefinition().getStatsDefinition().getCombatSkillsDefinition().getStrength();
    }
}
