package com.terryscape.entity.packet;

import com.terryscape.entity.EntityImpl;
import com.terryscape.net.OutgoingPacket;

import java.io.OutputStream;

public class EntityRemovedOutgoingPacket implements OutgoingPacket {

    private EntityImpl entity;

    public EntityRemovedOutgoingPacket setEntity(EntityImpl entity) {
        this.entity = entity;
        return this;
    }

    @Override
    public String getPacketName() {
        return "server_client_entity_removed";
    }

    @Override
    public void writePacket(OutputStream packet) {
        entity.writeEntityRemovedPacket(packet);
    }
}
