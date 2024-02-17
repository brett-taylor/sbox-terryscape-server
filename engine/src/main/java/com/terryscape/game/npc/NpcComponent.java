package com.terryscape.game.npc;

import com.terryscape.cache.npc.NpcDefinition;
import com.terryscape.entity.component.NetworkedEntityComponent;

public interface NpcComponent extends NetworkedEntityComponent {

    NpcDefinition getNpcDefinition();

}
