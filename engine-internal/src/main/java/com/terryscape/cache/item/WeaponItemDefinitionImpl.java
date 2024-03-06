package com.terryscape.cache.item;

import com.terryscape.game.combat.DamageType;

public class WeaponItemDefinitionImpl implements WeaponItemDefinition {

    private DamageType damageType;

    private String mainHandAttackAnimation;

    private String offHandAttackAnimation;

    @Override
    public DamageType getDamageType() {
        return damageType;
    }

    public WeaponItemDefinitionImpl setDamageType(DamageType damageType) {
        this.damageType = damageType;
        return this;
    }

    @Override
    public String getMainHandAttackAnimation() {
        return mainHandAttackAnimation;
    }

    public WeaponItemDefinitionImpl setMainHandAttackAnimation(String mainHandAttackAnimation) {
        this.mainHandAttackAnimation = mainHandAttackAnimation;
        return this;
    }

    @Override
    public String getOffHandAttackAnimation() {
        return offHandAttackAnimation;
    }

    public WeaponItemDefinitionImpl setOffHandAttackAnimation(String offHandAttackAnimation) {
        this.offHandAttackAnimation = offHandAttackAnimation;
        return this;
    }
}
