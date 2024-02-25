package com.terryscape.cache.item;

public interface ItemDefinition {

    String getId();

    String getName();

    String getDescription();

    boolean isStackable();

    String getAnimationMainHandAttack();

    String getAnimationOffHandAttack();

}
