package com.terryscape.game.world;

import com.terryscape.maths.Vector2Int;
import com.terryscape.net.IncomingPacket;
import com.terryscape.net.OutgoingPacket;
import com.terryscape.maths.RandomUtil;

import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

public enum Direction {
    NORTH(90, new Vector2Int(0, 1)),
    NORTH_EAST(45, new Vector2Int(1, 1)),
    EAST(0, new Vector2Int(1, 0)),
    SOUTH_EAST(315, new Vector2Int(1, -1)),
    SOUTH(270, new Vector2Int(0, -1)),
    SOUTH_WEST(225, new Vector2Int(-1, -1)),
    WEST(180, new Vector2Int(-1, 0)),
    NORTH_WEST(135, new Vector2Int(-1, 1));

    private final int rotation;

    private final Vector2Int asCoordinate;

    Direction(int rotation, Vector2Int asCoordinate) {
        this.rotation = rotation;
        this.asCoordinate = asCoordinate;
    }

    public int getRotation() {
        return rotation;
    }

    public Vector2Int asCoordinates() {
        return asCoordinate;
    }

    public static Direction fromRotation(int rotation) {
        return Arrays.stream(values())
            .filter(direction -> direction.rotation == rotation)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Invalid Rotation %s".formatted(rotation)));
    }

    public static Direction random() {
        return RandomUtil.randomCollection(Direction.values());
    }

    public static void writeToPacket(OutputStream packet, Direction direction) {
        OutgoingPacket.writeInt32(packet, direction.getRotation());
    }

    public static Direction readFromPacket(ByteBuffer packet) {
        return Direction.fromRotation(IncomingPacket.readInt32(packet));
    }
}
