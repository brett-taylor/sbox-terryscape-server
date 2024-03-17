package com.terryscape.cache.npc;

import com.terryscape.game.combat.DamageType;

import java.util.Optional;

public class NpcDefinitionImpl implements NpcDefinition {

    private String id;

    private String name;

    private String description;

    private boolean interactable;

    private boolean attackable;

    private NpcDefinitionNpcAppearanceType appearanceType;

    private NpcDefinitionSimpleNpcImpl simpleNpc;

    private NpcStatsDefinition statsDefinition;

    private int combatLevel;

    private DamageType combatDamageType;

    @Override
    public String getId() {
        return id;
    }

    public NpcDefinitionImpl setId(String id) {
        this.id = id;
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    public NpcDefinitionImpl setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public NpcDefinitionImpl setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public boolean isInteractable() {
        return interactable;
    }

    public NpcDefinitionImpl setInteractable(boolean interactable) {
        this.interactable = interactable;
        return this;
    }

    @Override
    public boolean isAttackable() {
        return attackable;
    }

    public NpcDefinitionImpl setAttackable(boolean attackable) {
        this.attackable = attackable;
        return this;
    }

    @Override
    public NpcDefinitionNpcAppearanceType getAppearanceType() {
        return appearanceType;
    }

    public NpcDefinitionImpl setAppearanceType(NpcDefinitionNpcAppearanceType appearanceType) {
        this.appearanceType = appearanceType;
        return this;
    }

    @Override
    public Optional<NpcDefinitionSimpleNpc> getSimpleNpc() {
        return Optional.ofNullable(simpleNpc);
    }

    public NpcDefinitionImpl setSimpleNpc(NpcDefinitionSimpleNpcImpl simpleNpc) {
        this.simpleNpc = simpleNpc;
        return this;
    }

    @Override
    public NpcStatsDefinition getStatsDefinition() {
        return statsDefinition;
    }

    public NpcDefinitionImpl setStatsDefinition(NpcStatsDefinition statsDefinition) {
        this.statsDefinition = statsDefinition;
        return this;
    }

    @Override
    public int getCombatLevel() {
        return combatLevel;
    }

    public NpcDefinitionImpl setCombatLevel(int combatLevel) {
        this.combatLevel = combatLevel;
        return this;
    }

    @Override
    public DamageType getCombatDamageType() {
        return combatDamageType;
    }

    public NpcDefinitionImpl setCombatDamageType(DamageType combatDamageType) {
        this.combatDamageType = combatDamageType;
        return this;
    }

    @Override
    public String toString() {
        return "NpcDefinition(id=%s)".formatted(id);
    }
}
