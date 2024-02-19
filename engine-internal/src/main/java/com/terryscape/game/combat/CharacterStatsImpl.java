package com.terryscape.game.combat;

import com.terryscape.entity.Entity;
import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.game.combat.health.AttackType;
import com.terryscape.game.combat.health.DamageType;

import java.util.Hashtable;

public class CharacterStatsImpl extends BaseEntityComponent {
    private int Attack, Defense, Mage, Range, Melee;
    private Hashtable<DamageType, Integer> AttackBonuses, DefenseBonuses;

    public CharacterStatsImpl(Entity entity) {
        super(entity);
        Attack = 50;
        Defense = 100;
        Mage = Range = Melee = 2;
        AttackBonuses = new Hashtable<>();
        DefenseBonuses = new Hashtable<>();

        AttackBonuses.put(DamageType.TYPELESS, 0);
        DefenseBonuses.put(DamageType.TYPELESS, 0);
    }

    public int GetEvasion(DamageType type) {
        int baseEvasion = Defense;
        baseEvasion += DefenseBonuses.getOrDefault(type, 0);
        return baseEvasion;
    }

    public int GetAccuracy(DamageType type) {
        int baseAccuracy = Attack;
        baseAccuracy += AttackBonuses.getOrDefault(type, 0);
        return baseAccuracy;
    }

    public int GetProficiency(DamageType type) {
        AttackType attackType = DamageType.GetAttackType(type);
        return switch (attackType){
            case MELEE -> Melee;
            case BOW -> Range;
            case MAGIC -> Mage;
            default -> 0;
        };
    }

    public void SetProficiency(AttackType attackType, int amount){
        switch (attackType){
            case MELEE -> Melee = amount;
            case BOW -> Range = amount;
            case MAGIC -> Mage = amount;
        }
    }

    public void AddDefenseBonus(DamageType type, int amount){
        int currentBonus = DefenseBonuses.getOrDefault(type, 0);
        DefenseBonuses.put(type, currentBonus + amount);
    }
}
