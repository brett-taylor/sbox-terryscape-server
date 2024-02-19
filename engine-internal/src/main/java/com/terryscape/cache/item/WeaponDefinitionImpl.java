package com.terryscape.cache.item;

import com.terryscape.game.combat.health.AttackType;
import com.terryscape.game.combat.health.DamageType;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class WeaponDefinitionImpl extends ItemDefinitionImpl implements WeaponDefinition {
    private long nextAttackOpportunity;
    private int attackDelay;

    private AttackType primaryAttribute;
    private int primaryAttributeBonus;
    private List<Pair<DamageType, Integer>> bonuses;
    private DamageType damageType;
    private String mainHandAttackAnimation, offHandAttackAnimation;

    public WeaponDefinitionImpl setPrimaryAttribute(AttackType attackType){
        primaryAttribute = attackType;
        return this;
    }

    public WeaponDefinitionImpl setAttributeBonus(int bonusAmount){
        primaryAttributeBonus = bonusAmount;
        return this;
    }

    public WeaponDefinitionImpl setBonuses(List<Pair<DamageType, Integer>> bonuses){
        this.bonuses = bonuses;
        return this;
    }

    public WeaponDefinitionImpl setDamageType(DamageType damageType){
        this.damageType = damageType;
        return this;
    }

    public WeaponDefinitionImpl setAttackAnimation(String attackAnimation, boolean mainHand){
        if(mainHand) {
            mainHandAttackAnimation = attackAnimation;
        } else {
            offHandAttackAnimation = attackAnimation;
        }
        return this;
    }

    public WeaponDefinitionImpl setAttackDelay(int delay){
        attackDelay = delay;
        return this;
    }

    @Override
    public Boolean attack(long currentTick) {
        var canAttack = nextAttackOpportunity <= currentTick;
        if(canAttack) {
            nextAttackOpportunity = currentTick + attackDelay;
        }
        return canAttack;
    }

    @Override
    public AttackType getPrimaryAttribute() {
        return primaryAttribute;
    }

    @Override
    public int getPrimaryAttributeBonus() {
        return primaryAttributeBonus;
    }

    @Override
    public List<Pair<DamageType, Integer>> getBonuses() {
        return bonuses;
    }

    @Override
    public DamageType getDamageType() {
        return damageType;
    }

    @Override
    public String getAttackAnimation(boolean mainHand) {
        return (mainHand) ? mainHandAttackAnimation : offHandAttackAnimation;
    }
}
