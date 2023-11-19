package com.terryscape.entity;

import com.terryscape.net.IncomingPacket;
import com.terryscape.net.OutgoingPacket;
import com.terryscape.net.PacketSerializable;

import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.UUID;

public class EntityIdentifier implements PacketSerializable {

    private final UUID identifier;

    private EntityIdentifier(UUID identifier) {
        this.identifier = identifier;
    }

    public String getValue() {
        return identifier.toString();
    }

    @Override
    public String toString() {
        return "EntityIdentifier(%s)".formatted(getValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EntityIdentifier that = (EntityIdentifier) o;

        return Objects.equals(identifier, that.identifier);
    }

    @Override
    public int hashCode() {
        return identifier != null ? identifier.hashCode() : 0;
    }

    @Override
    public void writeToPacket(OutputStream packet) {
        OutgoingPacket.writeString(packet, getValue());
    }

    public static EntityIdentifier readFromPacket(ByteBuffer packet) {
        var identifier = IncomingPacket.readString(packet);
        return new EntityIdentifier(UUID.fromString(identifier));
    }

    public static EntityIdentifier randomIdentifier() {
        return new EntityIdentifier(UUID.randomUUID());
    }
}
