package com.terryscape.entity.packet;

import com.terryscape.entity.EntityImpl;
import com.terryscape.net.OutgoingPacket;

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
        entity.writeEntityAddedPacket(packet);
    }
}
