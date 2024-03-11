package com.terryscape.net;

import com.terryscape.Config;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collection;

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

    static void writeFloat(OutputStream packet, float f) {
        try {
            packet.write(ByteBuffer.allocate(4).putFloat(f).array());
        } catch (IOException e) {
            LogManager.getLogger(OutgoingPacket.class).error("Failed writing float", e);
        }
    }

    static void writeBoolean(OutputStream packet, boolean bool) {
        writeInt32(packet, bool ? 1 : 0);
    }

    static void writeEnum(OutputStream packet, Enum<?> enumValue) {
        writeString(packet, enumValue.name());
    }

    static void writeCollection(OutputStream packet, Collection<? extends PacketSerializable> collection) {
        OutgoingPacket.writeInt32(packet, collection.size());
        collection.forEach(damageInformation -> damageInformation.writeToPacket(packet));
    }

    static <T extends PacketSerializable> void writeArray(OutputStream packet, T[] array) {
        Arrays.stream(array).forEach(elem -> {
            if (elem == null) {
                OutgoingPacket.writeString(packet, null);
            } else {
                elem.writeToPacket(packet);
            }
        });
    }
}
