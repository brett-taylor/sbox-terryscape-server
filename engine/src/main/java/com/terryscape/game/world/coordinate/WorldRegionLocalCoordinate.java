package com.terryscape.game.world.coordinate;

import com.terryscape.maths.Vector2Int;

import java.util.Objects;

public class WorldRegionLocalCoordinate {

    private final Vector2Int vector2Int;

    public WorldRegionLocalCoordinate(int x, int y) {
        this.vector2Int = new Vector2Int(x, y);
    }

    public int getX() {
        return vector2Int.getX();
    }

    public int getY() {
        return vector2Int.getY();
    }

    public WorldCoordinate toWorldCoordinate(WorldRegionCoordinate worldRegionCoordinate) {
        return new WorldCoordinate(
            getX() + worldRegionCoordinate.toWorldCoordinateOrigin().getX(),
            getY() + worldRegionCoordinate.toWorldCoordinateOrigin().getY()
        );
    }

    @Override
    public String toString() {
        return "WorldRegionLocalCoordinate(x=%s,y=%s)".formatted(getX(), getY());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorldRegionLocalCoordinate that = (WorldRegionLocalCoordinate) o;
        return getX() == that.getX() && getY() == that.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }
}
