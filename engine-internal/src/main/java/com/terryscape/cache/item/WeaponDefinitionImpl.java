package com.terryscape.cache.item;

import com.terryscape.game.combat.health.AttackType;
import com.terryscape.game.combat.health.DamageType;
import com.terryscape.game.combat.SpecialAttackTrigger;
import com.terryscape.maths.RandomUtil;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class WeaponDefinitionImpl extends ItemDefinitionImpl implements WeaponDefinition {
    private long nextAttackOpportunity;
    private int attackDelay;

    private AttackType primaryAttribute;
    private int primaryAttributeBonus;
    private List<Pair<DamageType, Integer>> bonuses = new ArrayList<>();
    private DamageType damageType;
    private List<String> mainHandAttackAnimations, offHandAttackAnimations;
    private SpecialAttackTrigger specialAttack;

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

    public WeaponDefinitionImpl setAttackAnimations(List<String> attackAnimations) {
        mainHandAttackAnimations = offHandAttackAnimations = attackAnimations;
        return this;
    }

    public WeaponDefinitionImpl setAttackAnimations(List<String> attackAnimations, boolean mainHand){
        if(mainHand) {
            mainHandAttackAnimations = attackAnimations;
        } else {
            offHandAttackAnimations = attackAnimations;
        }
        return this;
    }

    public WeaponDefinitionImpl setSpecialAttack(SpecialAttackTrigger attack){
        specialAttack = attack;
        return this;
    }

    public SpecialAttackTrigger executeSpecialAttack(long currentTick) {
        if(specialAttack != null) {
            nextAttackOpportunity = currentTick + attackDelay;
        }
        return specialAttack;
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

    private String getRandomAnimation(List<String> animationList) {
        return RandomUtil.randomCollection(animationList);
    }

    @Override
    public String getAttackAnimation(boolean mainHand) {
        return getRandomAnimation((mainHand) ? mainHandAttackAnimations : offHandAttackAnimations);
    }
}
