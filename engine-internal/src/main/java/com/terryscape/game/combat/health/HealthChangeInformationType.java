package com.terryscape.game.combat.health;

import com.terryscape.game.combat.DamageType;

public enum HealthChangeInformationType {
    BLOCKED,
    DAMAGE,
    DAMAGE_MELEE,
    HEAL_FOOD;

    public static HealthChangeInformationType toHealthChangeReason(DamageType damageType) {
        return switch (damageType) {

            case STAB -> DAMAGE_MELEE;
            case SLASH -> DAMAGE_MELEE;
            case TYPELESS -> DAMAGE;
        };
    }

}
