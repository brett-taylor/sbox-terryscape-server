package com.terryscape.game.npc;

import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.game.combat.CombatBonusesProviderComponent;

public class NpcCombatBonusesProviderComponent extends BaseEntityComponent implements CombatBonusesProviderComponent {

    private final NpcComponent npcComponent;

    public NpcCombatBonusesProviderComponent(NpcComponent npcComponent) {

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
    public float getOffensiveAir() {
        return npcComponent.getNpcDefinition().getStatsDefinition().getCombatBonusesDefinition().getOffensiveAir();
    }

    @Override
    public float getOffensiveFire() {
        return npcComponent.getNpcDefinition().getStatsDefinition().getCombatBonusesDefinition().getOffensiveFire();
    }

    @Override
    public float getOffensiveArrow() {
        return npcComponent.getNpcDefinition().getStatsDefinition().getCombatBonusesDefinition().getOffensiveArrow();
    }

    @Override
    public float getDefensiveStab() {
        return npcComponent.getNpcDefinition().getStatsDefinition().getCombatBonusesDefinition().getDefensiveStab();
    }

    @Override
    public float getDefensiveSlash() {
        return npcComponent.getNpcDefinition().getStatsDefinition().getCombatBonusesDefinition().getDefensiveSlash();
    }

    @Override
    public float getDefensiveAir() {
        return npcComponent.getNpcDefinition().getStatsDefinition().getCombatBonusesDefinition().getDefensiveAir();
    }

    @Override
    public float getDefensiveFire() {
        return npcComponent.getNpcDefinition().getStatsDefinition().getCombatBonusesDefinition().getDefensiveFire();
    }

    @Override
    public float getDefensiveArrow() {
        return npcComponent.getNpcDefinition().getStatsDefinition().getCombatBonusesDefinition().getDefensiveArrow();
    }

    @Override
    public float getStrengthMelee() {
        return npcComponent.getNpcDefinition().getStatsDefinition().getCombatBonusesDefinition().getStrengthMelee();
    }

    @Override
    public float getStrengthMagic() {
        return npcComponent.getNpcDefinition().getStatsDefinition().getCombatBonusesDefinition().getStrengthMagic();
    }

    @Override
    public float getStrengthRange() {
        return npcComponent.getNpcDefinition().getStatsDefinition().getCombatBonusesDefinition().getStrengthRange();
    }
}
