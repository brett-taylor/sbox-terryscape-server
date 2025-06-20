package com.terryscape.world.coordinate;

import com.terryscape.Config;
import com.terryscape.maths.Vector2Int;
import com.terryscape.net.IncomingPacket;
import com.terryscape.net.OutgoingPacket;
import com.terryscape.net.PacketSerializable;
import com.terryscape.world.Direction;

import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class WorldCoordinate implements PacketSerializable {

    private final Vector2Int vector2Int;

    public WorldCoordinate(int x, int y) {
        this.vector2Int = new Vector2Int(x, y);
    }

    public WorldCoordinate(Vector2Int vector2Int) {
        this.vector2Int = vector2Int;
    }

    public int getX() {
        return vector2Int.getX();
    }

    public int getY() {
        return vector2Int.getY();
    }

    public WorldCoordinate add(WorldCoordinate other) {
        return new WorldCoordinate(vector2Int.add(other.vector2Int));
    }

    public float distance(WorldCoordinate other) {
        return vector2Int.distance(other.vector2Int);
    }

    public int tileDistance(WorldCoordinate other) {
        var absX = Math.abs(other.getX() - getX());
        var absY = Math.abs(other.getY() - getY());
        return Math.max(absX, absY);
    }

    public boolean isCardinal(WorldCoordinate other) {
        var absX = Math.abs(getX() - other.getX());
        var absY = Math.abs(getY() - other.getY());

        return absX + absY == 1;
    }

    public boolean isIntercardinal(WorldCoordinate other) {
        var absX = Math.abs(getX() - other.getX());
        var absY = Math.abs(getY() - other.getY());

        return absX == 1 && absY == 1;
    }

    public Direction directionTo(WorldCoordinate other) {
        var xDiff = Math.max(Math.min(other.getX() - getX(), 1), -1);
        var yDiff = Math.max(Math.min(other.getY() - getY(), 1), -1);
        var vector = new Vector2Int(xDiff, yDiff);

        return Arrays.stream(Direction.values())
            .filter(direction -> direction.asCoordinates().equals(vector))
            .findFirst()
            .orElse(Direction.NORTH);
    }

    public WorldCoordinate north() {
        return add(Direction.NORTH.asCoordinates());
    }

    public WorldCoordinate northEast() {
        return add(Direction.NORTH_EAST.asCoordinates());
    }

    public WorldCoordinate east() {
        return add(Direction.EAST.asCoordinates());
    }

    public WorldCoordinate southEast() {
        return add(Direction.SOUTH_EAST.asCoordinates());
    }

    public WorldCoordinate south() {
        return add(Direction.SOUTH.asCoordinates());
    }

    public WorldCoordinate southWest() {
        return add(Direction.SOUTH_WEST.asCoordinates());
    }

    public WorldCoordinate west() {
        return add(Direction.WEST.asCoordinates());
    }

    public WorldCoordinate northWest() {
        return add(Direction.NORTH_WEST.asCoordinates());
    }

    public WorldCoordinate[] getCardinalNeighbours() {
        return new WorldCoordinate[]{
            north(), east(), south(), west(),
        };
    }

    public WorldCoordinate[] getCardinalAndIntercardinalNeighbours() {
        return new WorldCoordinate[]{
            north(), east(), south(), west(), northEast(), southEast(), southWest(), northWest()
        };
    }

    public static WorldCoordinate getClosestWorldCoordinate(WorldCoordinate from, List<WorldCoordinate> worldCoordinates) {
        var closestDistance = Float.MAX_VALUE;
        var closestNeighbour = worldCoordinates.get(0);

        for (var neighbour : worldCoordinates) {
            var distance = from.distance(neighbour);
            if (distance < closestDistance) {
                closestDistance = distance;
                closestNeighbour = neighbour;
            }
        }

        return closestNeighbour;
    }

    public WorldCoordinate getClosestCardinalNeighbourFrom(WorldCoordinate from) {
        return getClosestWorldCoordinate(from, Arrays.stream(getCardinalNeighbours()).toList());
    }

    public WorldRegionCoordinate toWorldRegionCoordinate() {
        return new WorldRegionCoordinate(
            (int) Math.floor( getX() / (float) Config.WORLD_REGION_SIZE ),
            (int) Math.floor( getY() / (float) Config.WORLD_REGION_SIZE )
        );
    }

    public WorldRegionLocalCoordinate toWorldRegionLocalCoordinate() {
        var worldRegionCoordinate = toWorldRegionCoordinate().toWorldCoordinateOrigin();
        return new WorldRegionLocalCoordinate(
            getX() - worldRegionCoordinate.getX(),
            getY() - worldRegionCoordinate.getY()
        );
    }

    @Override
    public void writeToPacket(OutputStream packet) {
        OutgoingPacket.writeInt32(packet, getX());
        OutgoingPacket.writeInt32(packet, getY());
    }

    public static WorldCoordinate readFromPacket(ByteBuffer packet) {
        return new WorldCoordinate(IncomingPacket.readInt32(packet), IncomingPacket.readInt32(packet));
    }

    @Override
    public String toString() {
        return "WorldCoordinate(x=%s,y=%s)".formatted(getX(), getY());
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

    private WorldCoordinate add(Vector2Int other) {
        return new WorldCoordinate(vector2Int.add(other));
    }
}
