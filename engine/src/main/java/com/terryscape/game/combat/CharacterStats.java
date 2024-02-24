package com.terryscape.game.combat;

import com.terryscape.game.combat.health.DamageType;

public interface CharacterStats {
    int getEvasion(DamageType type);
    int getAccuracy(DamageType type);
    int getProficiency (DamageType type);
}
