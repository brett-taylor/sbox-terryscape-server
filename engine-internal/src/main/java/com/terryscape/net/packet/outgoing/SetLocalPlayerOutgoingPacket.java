package com.terryscape.net.packet.outgoing;

import com.terryscape.entity.EntityImpl;
import com.terryscape.net.packet.OutgoingPacket;

import java.io.OutputStream;

public class SetLocalPlayerOutgoingPacket implements OutgoingPacket {

    private EntityImpl localEntity;

    public SetLocalPlayerOutgoingPacket setLocalEntity(EntityImpl localEntity) {
        this.localEntity = localEntity;
        return this;
    }

    @Override
    public String getPacketName() {
        return "server_client_set_local_player";
    }

    @Override
    public void writePacket(OutputStream packet) {
        OutgoingPacket.writeEntityIdentifier(packet, localEntity.getIdentifier().orElseThrow());
    }
}
