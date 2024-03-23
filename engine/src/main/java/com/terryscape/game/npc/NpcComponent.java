package com.terryscape.game.npc;

import com.terryscape.cache.npc.NpcDefinition;
import com.terryscape.entity.component.BaseEntityComponent;

public class NpcComponent extends BaseEntityComponent {

    private NpcDefinition npcDefinition;

    public NpcDefinition getNpcDefinition() {
        return npcDefinition;
    }

    public void setNpcDefinition(NpcDefinition npcDefinition) {
        this.npcDefinition = npcDefinition;
    }
}
