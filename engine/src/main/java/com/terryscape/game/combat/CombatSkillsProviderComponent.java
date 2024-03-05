package com.terryscape.game.combat;

import com.terryscape.entity.component.EntityComponent;

public interface CombatSkillsProviderComponent extends EntityComponent {

    int getAttack();

    int getDefence();

    int getStrength();

}
