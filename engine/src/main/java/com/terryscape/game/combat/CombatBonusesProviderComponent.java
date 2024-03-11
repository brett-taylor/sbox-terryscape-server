package com.terryscape.game.combat;

import com.terryscape.entity.component.EntityComponent;

public interface CombatBonusesProviderComponent extends EntityComponent {

    float getOffensiveStab();

    float getOffensiveSlash();

    float getOffensiveAir();

    float getOffensiveFire();

    float getDefensiveStab();

    float getDefensiveSlash();

    float getDefensiveAir();

    float getDefensiveFire();

    float getStrengthMelee();

    float getStrengthMagic();
}
