package com.terryscape.world;

import com.terryscape.util.RandomUtil;

import java.util.Arrays;

public enum Direction {
    NORTH(90, new WorldCoordinate(0, 1)),
    NORTH_EAST(45, new WorldCoordinate(1, 1)),
    EAST(0, new WorldCoordinate(1, 0)),
    SOUTH_EAST(315, new WorldCoordinate(1, -1)),
    SOUTH(270, new WorldCoordinate(0, -1)),
    SOUTH_WEST(225, new WorldCoordinate(-1, -1)),
    WEST(180, new WorldCoordinate(-1, 0)),
    NORTH_WEST(135, new WorldCoordinate(-1, 1));

    private final int rotation;

    private final WorldCoordinate worldCoordinate;

    Direction(int rotation, WorldCoordinate worldCoordinate) {
        this.rotation = rotation;
        this.worldCoordinate = worldCoordinate;
    }

    public int getRotation() {
        return rotation;
    }

    public WorldCoordinate toWorldCoordinate() {
        return worldCoordinate;
    }

    public static Direction fromRotation(int rotation) {
        return Arrays.stream(values())
            .filter(direction -> direction.rotation == rotation)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Invalid Rotation %s".formatted(rotation)));
    }

    public static Direction random() {
        var random = RandomUtil.randomNumber(0, Direction.values().length - 1);
        return Direction.values()[random];
    }
}
