package com.terryscape.net;

import com.terryscape.Config;
import com.terryscape.entity.EntityIdentifier;
import com.terryscape.net.Client;
import com.terryscape.world.Direction;
import com.terryscape.world.WorldCoordinate;

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

    static WorldCoordinate readWorldCoordinate(ByteBuffer packet) {
        return new WorldCoordinate(readInt32(packet), readInt32(packet));
    }

    static EntityIdentifier readEntityIdentifier(ByteBuffer packet) {
        return EntityIdentifier.fromString(readString(packet));
    }

    static Direction readDirection(ByteBuffer packet) {
        return Direction.fromRotation(readInt32(packet));
    }
}