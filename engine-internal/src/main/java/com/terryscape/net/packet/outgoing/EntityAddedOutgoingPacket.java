package com.terryscape.net.packet.outgoing;

import com.terryscape.entity.EntityImpl;
import com.terryscape.net.packet.OutgoingPacket;

import java.io.OutputStream;

public class EntityAddedOutgoingPacket implements OutgoingPacket {

    private EntityImpl entity;

    public EntityAddedOutgoingPacket setEntity(EntityImpl entity) {
        this.entity = entity;
        return this;
    }

    @Override
    public String getPacketName() {
        return "server_client_entity_added";
    }

    @Override
    public void writePacket(OutputStream packet) {
        OutgoingPacket.writeEntityIdentifier(packet, entity.getIdentifier().orElseThrow());
        entity.writeEntityAddedPacket(packet);
    }
}
