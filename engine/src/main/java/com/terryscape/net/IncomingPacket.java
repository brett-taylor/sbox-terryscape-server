package com.terryscape.net;

import com.terryscape.Config;

import java.nio.ByteBuffer;

public interface IncomingPacket {

    String getPacketName();

    void handlePacket(Client client, ByteBuffer packet);

    static String readString(ByteBuffer packet) {
        var stringLength = readInt32(packet);
        var stringBytes = new byte[stringLength];

        packet.get(stringBytes);
        return new String(stringBytes, Config.CHARSET);
    }

    static int readInt32(ByteBuffer packet) {
        return packet.getInt();
    }

    static boolean readBoolean(ByteBuffer packet) {
        var int32 = readInt32(packet);
        return int32 == 1;
    }

    static <T extends Enum<T>> T readEnum(Class<T> enumClass, ByteBuffer packet) {
        var name = IncomingPacket.readString(packet);
        return Enum.valueOf(enumClass, name);
    }
}