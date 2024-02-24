package com.terryscape.game.combat;

import com.terryscape.cache.npc.CombatStats;
import com.terryscape.entity.Entity;
import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.game.combat.health.AttackType;
import com.terryscape.game.combat.health.DamageType;

import java.util.Hashtable;

public class CharacterStatsImpl extends BaseEntityComponent implements CharacterStats {
    private int Attack, Defense, Mage, Range, Melee;
    private final CombatStats itemBonuses;
    private final Hashtable<DamageType, Integer> AttackBonuses;
    private final Hashtable<DamageType, Integer> DefenseBonuses;

    public CharacterStatsImpl(Entity entity) {
        super(entity);
        Attack = 50;
        Defense = 100;
        Mage = Range = Melee = 2;
        AttackBonuses = new Hashtable<>();
        DefenseBonuses = new Hashtable<>();
        itemBonuses = new CombatStats();
    }

    public void SetStats(CombatStats stats) {
        Attack = stats.Attack;
        Defense = stats.Defense;
        Mage = stats.Mage;
        Range = stats.Range;
        Melee = stats.Melee;
    }

    public int getEvasion(DamageType type) {
        int baseEvasion = Defense;
        baseEvasion += DefenseBonuses.getOrDefault(type, 0);
        return baseEvasion;
    }

    public int getAccuracy(DamageType type) {
        int baseAccuracy = Attack;
        baseAccuracy += AttackBonuses.getOrDefault(type, 0);
        return baseAccuracy;
    }

    public int getProficiency(DamageType type) {
        AttackType attackType = DamageType.GetAttackType(type);
        return switch (attackType){
            case MELEE -> Melee + itemBonuses.Melee;
            case BOW -> Range + itemBonuses.Range;
            case MAGIC -> Mage + itemBonuses.Mage;
            //TODO: Change the default back to 0
            default -> Melee;
        };
    }

    public void SetProficiency(AttackType attackType, int amount){
        switch (attackType){
            case MELEE -> Melee = amount;
            case BOW -> Range = amount;
            case MAGIC -> Mage = amount;
        }
    }
    public void AdjustWeaponProficiency(AttackType attackType, int amount){
        switch (attackType){
            case MELEE -> itemBonuses.Melee += amount;
            case BOW -> itemBonuses.Range = amount;
            case MAGIC -> itemBonuses.Mage = amount;
        }
    }

    public void AddDefenseBonus(DamageType type, int amount){
        int currentBonus = DefenseBonuses.getOrDefault(type, 0);
        DefenseBonuses.put(type, currentBonus + amount);
    }

    public void AddAttackBonus(DamageType type, int amount) {
        int currentBonus = AttackBonuses.getOrDefault(type, 0);
        AttackBonuses.put(type, currentBonus + amount);
    }

    public void RemoveDefenseBonus(DamageType type, int amount){
        int currentBonus = DefenseBonuses.getOrDefault(type, 0);
        DefenseBonuses.put(type, currentBonus - amount);
    }

    public void RemoveAttackBonus(DamageType type, int amount) {
        int currentBonus = AttackBonuses.getOrDefault(type, 0);
        AttackBonuses.put(type, currentBonus - amount);
    }
}
