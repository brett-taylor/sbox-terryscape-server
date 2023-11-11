package com.terryscape.world;

import java.util.Arrays;
import java.util.Objects;

public class WorldCoordinate {

    private final int x;

    private final int y;

    public WorldCoordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public WorldCoordinate add(WorldCoordinate other) {
        return add(other.getX(), other.getY());
    }

    public WorldCoordinate add(int x, int y) {
        return new WorldCoordinate(getX() + x, getY() + y);
    }

    @Override
    public String toString() {
        return "WorldCoordinate(x=%s,y=%s)".formatted(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorldCoordinate that = (WorldCoordinate) o;
        return getX() == that.getX() && getY() == that.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }

    public Direction directionTo(WorldCoordinate other) {
        var xDiff = Math.max(Math.min(other.getX() - getX(), 1), -1);
        var yDiff = Math.max(Math.min(other.getY() - getY(), 1), -1);
        var worldCoordinate = new WorldCoordinate(xDiff, yDiff);

        return Arrays.stream(Direction.values())
            .filter(direction -> direction.toWorldCoordinate().equals(worldCoordinate))
            .findFirst()
            .orElse(Direction.NORTH);
    }
}
