package com.terryscape.cache.item;

public class WeaponItemDefinitionImpl implements WeaponItemDefinition {

    private String mainHandAttackAnimation;

    private String offHandAttackAnimation;

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
