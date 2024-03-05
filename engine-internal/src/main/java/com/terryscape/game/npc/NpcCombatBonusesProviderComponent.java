package com.terryscape.game.npc;

import com.terryscape.entity.Entity;
import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.game.combat.CombatBonusesProviderComponent;

public class NpcCombatBonusesProviderComponent extends BaseEntityComponent implements CombatBonusesProviderComponent {

    private final NpcComponent npcComponent;

    public NpcCombatBonusesProviderComponent(Entity entity, NpcComponent npcComponent) {
        super(entity);

        this.npcComponent = npcComponent;
    }

    @Override
    public float getOffensiveStab() {
        return npcComponent.getNpcDefinition().getStatsDefinition().getCombatBonusesDefinition().getOffensiveStab();
    }

    @Override
    public float getOffensiveSlash() {
        return npcComponent.getNpcDefinition().getStatsDefinition().getCombatBonusesDefinition().getOffensiveSlash();
    }

    @Override
    public float getDefensiveStab() {
        return npcComponent.getNpcDefinition().getStatsDefinition().getCombatBonusesDefinition().getDefensiveStab();
    }

    @Override
    public float getDefensiveSlash() {
        return npcComponent.getNpcDefinition().getStatsDefinition().getCombatBonusesDefinition().getDefensiveSlash();
    }
}
