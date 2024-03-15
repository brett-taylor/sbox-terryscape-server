package com.terryscape.game.combat.health;

import com.terryscape.game.combat.DamageType;

public enum HealthChangeInformationType {
    BLOCKED,
    DAMAGE,
    DAMAGE_MELEE,
    DAMAGE_MAGIC,
    DAMAGE_RANGE,
    HEAL_FOOD;

    public static HealthChangeInformationType toHealthChangeReason(DamageType damageType) {
        return switch (damageType) {
            case STAB, SLASH -> DAMAGE_MELEE;

            case AIR, FIRE -> DAMAGE_MAGIC;

            case ARROW -> DAMAGE_RANGE;

            case TYPELESS -> DAMAGE;
        };
    }

}
