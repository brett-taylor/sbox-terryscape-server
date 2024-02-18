package com.terryscape.game.combat.health;

public enum DamageType {
    SLASH,
    SMASH,
    PIERCE,
    THROWN,
    SHOT,
    DRAWN,
    FIRE,
    WATER,
    EARTH,
    AIR,
    TYPELESS;

    public static AttackType GetAttackType(DamageType type) {
        return switch (type) {
            case SLASH, SMASH, PIERCE -> AttackType.MELEE;
            case THROWN, SHOT, DRAWN -> AttackType.BOW;
            case FIRE, WATER, EARTH, AIR -> AttackType.MAGIC;
            default -> AttackType.TYPELESS;
        };
    }
}
