package com.terryscape.cache.npc;

import com.terryscape.game.combat.health.DamageType;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NpcDefinitionImpl implements NpcDefinition {
    private DamageType damageType;
    private CombatStats baseStats;
    private List<Pair<DamageType, Integer>> attackBonuses, defenseBonuses;

    private String id;

    private String name;

    private String description;

    private boolean interactable;

    private boolean attackable;

    private NpcDefinitionNpcAppearanceType appearanceType;

    private NpcDefinitionSimpleNpcImpl simpleNpc;

    public NpcDefinitionImpl(){
        attackBonuses = new ArrayList<>();
        defenseBonuses = new ArrayList<>();
        baseStats = new CombatStats();
        damageType = DamageType.TYPELESS;
    }

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

    public NpcDefinitionImpl setDamageType(DamageType damageType){
        this.damageType = damageType;
        return this;
    }

    public NpcDefinitionImpl setCombatStats(CombatStats stats){
        this.baseStats = stats;
        return this;
    }

    public NpcDefinitionImpl setAttackBonuses(List<Pair<DamageType, Integer>> attackBonuses){
        this.attackBonuses = attackBonuses;
        return this;
    }

    public NpcDefinitionImpl setDefenseBonuses(List<Pair<DamageType, Integer>> defenseBonuses){
        this.defenseBonuses = defenseBonuses;
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

    @Override
    public DamageType getDamageType() {
        return damageType;
    }

    @Override
    public CombatStats getCombatStats() {
        return baseStats;
    }

    @Override
    public List<Pair<DamageType, Integer>> getAttackBonuses() {
        return attackBonuses;
    }

    @Override
    public List<Pair<DamageType, Integer>> getDefenseBonuses() {
        return defenseBonuses;
    }

    public NpcDefinitionImpl setSimpleNpc(NpcDefinitionSimpleNpcImpl simpleNpc) {
        this.simpleNpc = simpleNpc;
        return this;
    }

    @Override
    public String toString() {
        return "NpcDefinition(id=%s)".formatted(id);
    }
}
