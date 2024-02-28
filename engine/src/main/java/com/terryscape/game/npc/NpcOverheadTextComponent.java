package com.terryscape.game.npc;

import com.terryscape.entity.component.EntityComponent;

public interface NpcOverheadTextComponent extends EntityComponent {

    void say(String message);

}
