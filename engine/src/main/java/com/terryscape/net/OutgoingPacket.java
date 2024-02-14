package com.terryscape.net;

import com.terryscape.Config;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public interface OutgoingPacket {

    String getPacketName();

    void writePacket(OutputStream packet);

    static void writeString(OutputStream packet, String string) {
        if (string == null) {
            writeInt32(packet, -1);
            return;
        }

        try {
            var bytes = string.getBytes(Config.CHARSET);
            writeInt32(packet, bytes.length);
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

    static void writeEnum(OutputStream packet, Enum<?> enumValue) {
        writeString(packet, enumValue.name());
    }
}
