package com.terryscape.game.player;

import com.terryscape.entity.component.NetworkedEntityComponent;
import com.terryscape.game.combat.CombatSkillsProviderComponent;

public interface PlayerSkillsComponent extends CombatSkillsProviderComponent, NetworkedEntityComponent {

    int getCombat();

    void setAttack(int attack);

    void setDefence(int defence);

    void setStrength(int strength);

    void setMagic(int magic);

    void setRange(int range);

    int getConstitution();

    void setConstitution(int constitution);

}
