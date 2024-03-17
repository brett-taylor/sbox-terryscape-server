package com.terryscape.game.world.coordinate;

import com.terryscape.Config;
import com.terryscape.maths.Vector2Int;
import com.terryscape.net.IncomingPacket;
import com.terryscape.game.world.Direction;

import java.nio.ByteBuffer;
import java.util.Objects;

public class WorldRegionCoordinate {

    private final Vector2Int vector2Int;

    public WorldRegionCoordinate(int x, int y) {
        this.vector2Int = new Vector2Int(x, y);
    }

    private WorldRegionCoordinate(Vector2Int vector2Int) {
        this(vector2Int.getX(), vector2Int.getY());
    }

    public int getX() {
        return vector2Int.getX();
    }

    public int getY() {
        return vector2Int.getY();
    }

    public WorldRegionCoordinate north() {
        return add(Direction.NORTH.asCoordinates());
    }

    public WorldRegionCoordinate northEast() {
        return add(Direction.NORTH_EAST.asCoordinates());
    }

    public WorldRegionCoordinate east() {
        return add(Direction.EAST.asCoordinates());
    }

    public WorldRegionCoordinate southEast() {
        return add(Direction.SOUTH_EAST.asCoordinates());
    }

    public WorldRegionCoordinate south() {
        return add(Direction.SOUTH.asCoordinates());
    }

    public WorldRegionCoordinate southWest() {
        return add(Direction.SOUTH_WEST.asCoordinates());
    }

    public WorldRegionCoordinate west() {
        return add(Direction.WEST.asCoordinates());
    }

    public WorldRegionCoordinate northWest() {
        return add(Direction.NORTH_WEST.asCoordinates());
    }

    public WorldRegionCoordinate[] getCardinalAndIntercardinalNeighbours() {
        return new WorldRegionCoordinate[]{
            north(), east(), south(), west(), northEast(), southEast(), southWest(), northWest()
        };
    }

    public WorldCoordinate toWorldCoordinateOrigin() {
        return new WorldCoordinate(getX() * Config.WORLD_REGION_SIZE, getY() * Config.WORLD_REGION_SIZE);
    }

    @Override
    public String toString() {
        return "WorldRegionCoordinate(x=%s,y=%s)".formatted(getX(), getY());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorldRegionCoordinate that = (WorldRegionCoordinate) o;
        return getX() == that.getX() && getY() == that.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }

    public static WorldRegionCoordinate readFromPacket(ByteBuffer packet) {
        return new WorldRegionCoordinate(IncomingPacket.readInt32(packet), IncomingPacket.readInt32(packet));
    }

    private WorldRegionCoordinate add(Vector2Int other) {
        return new WorldRegionCoordinate(vector2Int.add(other));
    }
}
