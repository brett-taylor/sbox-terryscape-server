package com.terryscape.game.combat;

import com.terryscape.entity.component.EntityComponent;

public interface CombatBonusesProviderComponent extends EntityComponent {

    float getOffensiveStab();

    float getOffensiveSlash();

    float getDefensiveStab();

    float getDefensiveSlash();

    float getStrengthMelee();
}
