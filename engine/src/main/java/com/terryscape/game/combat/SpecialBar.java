package com.terryscape.game.combat;

import com.terryscape.entity.component.EntityComponent;

public interface SpecialBar extends EntityComponent {
    boolean canUse(int amount);
    void use(int amount);
}
