package com.terryscape.game.npc;

import com.terryscape.entity.Entity;
import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.net.PacketManager;

import static com.terryscape.game.chat.GameMessageOutgoingPacket.npcOverheadChat;

public class NpcOverheadTextComponentImpl extends BaseEntityComponent implements NpcOverheadTextComponent {

    private final PacketManager packetManager;

    public NpcOverheadTextComponentImpl(Entity entity, PacketManager packetManager) {
        super(entity);
        this.packetManager = packetManager;
    }

    @Override
    public void say(String message) {
        var npc = getEntity().getComponentOrThrow(NpcComponent.class);
        packetManager.broadcast(npcOverheadChat(npc, message));
    }
}
