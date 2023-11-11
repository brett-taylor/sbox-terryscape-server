package com.terryscape.net.packet;

import com.terryscape.Config;
import com.terryscape.entity.EntityIdentifier;
import com.terryscape.world.Direction;
import com.terryscape.world.WorldCoordinate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.UUID;

public interface OutgoingPacket {

    String getPacketName();

    void writePacket(OutputStream packet);

    static void writeString(OutputStream packet, String string) {
        try {
            var bytes = string.getBytes(Config.CHARSET);
            packet.write(ByteBuffer.allocate(4).putInt(bytes.length).array());
            packet.write(bytes);
        } catch (IOException e) {
            LogManager.getLogger(OutgoingPacket.class).error("Failed writing string", e);
        }
    }

    static void writeInt32(OutputStream packet, int int32) {
        try {
            packet.write(ByteBuffer.allocate(4).putInt(int32).array());
        } catch (IOException e) {
            LogManager.getLogger(OutgoingPacket.class).error("Failed writing int32", e);
        }
    }

    static void writeBoolean(OutputStream packet, boolean bool) {
        writeInt32(packet, bool ? 1 : 0);
    }

    static void writeWorldCoordinate(OutputStream packet, WorldCoordinate worldCoordinate) {
        writeInt32(packet, worldCoordinate.getX());
        writeInt32(packet, worldCoordinate.getY());
    }

    static void writeEntityIdentifier(OutputStream packet, EntityIdentifier entityIdentifier) {
        writeString(packet, entityIdentifier.getValue());
    }

    static void writeDirection(OutputStream packet, Direction direction ) {
        writeInt32(packet, direction.getRotation());
    }
}
