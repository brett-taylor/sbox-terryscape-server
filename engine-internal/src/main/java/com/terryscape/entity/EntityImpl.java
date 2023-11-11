package com.terryscape.entity;

import com.terryscape.net.packet.OutgoingPacket;

import java.io.OutputStream;
import java.util.Optional;

public abstract class EntityImpl implements Entity {

    private EntityIdentifier entityIdentifier;

    @Override
    public Optional<EntityIdentifier> getIdentifier() {
        return Optional.ofNullable(entityIdentifier);
    }

    public void setEntityIdentifier(EntityIdentifier entityIdentifier) {
        this.entityIdentifier = entityIdentifier;
    }

    public void tick() {
    }

    public void register() {
    }

    public void spawn() {
    }

    public void writeEntityAddedPacket(OutputStream packet) {
        OutgoingPacket.writeString(packet, getEntityType().name());
    }

    public void writeEntityUpdatedPacket(OutputStream packet) {
    }

}
