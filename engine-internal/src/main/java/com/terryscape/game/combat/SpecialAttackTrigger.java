package com.terryscape.game.combat;
import com.terryscape.entity.Entity;
import com.terryscape.game.combat.health.DamageType;

public class SpecialAttackTrigger {
    private SpecialAttackCache attack;
    private String animationName;
    private int energyCost, accuracyBonus, damageBonus;
    private DamageType damageType;

    public SpecialAttackTrigger setAttack(SpecialAttackCache attack) {
        this.attack = attack;
        return this;
    }
    public SpecialAttackTrigger setAnimationName(String name) {
        animationName = name;
        return this;
    }
    public SpecialAttackTrigger setEnergyCost(int cost) {
        energyCost = cost;
        return this;
    }
    public SpecialAttackTrigger setAccuracyBonus(int bonus) {
        accuracyBonus = bonus;
        return this;
    }
    public SpecialAttackTrigger setDamageBonus(int bonus) {
        damageBonus = bonus;
        return this;
    }
    public SpecialAttackTrigger setDamageType(DamageType type) {
        damageType = type;
        return this;
    }
    public void execute(Entity attacker, Entity victim) {
        attack.specialAttack.accept(attacker, victim, this);
    }

    public DamageType getDamageType() {
        return damageType;
    }

    public int getDamageBonus() {
        return damageBonus;
    }

    public int getAccuracyBonus() {
        return accuracyBonus;
    }

    public int getEnergyCost() {
        return energyCost;
    }

    public String getAnimationName() {
        return animationName;
    }
}
