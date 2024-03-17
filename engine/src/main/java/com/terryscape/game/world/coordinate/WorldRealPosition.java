package com.terryscape.game.world.coordinate;

import com.terryscape.Config;

public class WorldRealPosition {

    public static WorldRealPosition fromJsonString(String jsonString) {
        var parts = jsonString.split(",");
        return new WorldRealPosition(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]));
    }

    private final double x;

    private final double y;

    private WorldRealPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public WorldCoordinate asWorldCoordinate() {
        return new WorldCoordinate(
            (int) Math.floor( getX() / Config.WORLD_TILE_SIZE ),
            (int) Math.floor( getY() / Config.WORLD_TILE_SIZE )
        );
    }

    public WorldRegionLocalCoordinate asWorldRegionLocalCoordinate() {
        return new WorldRegionLocalCoordinate(
            (int) Math.floor( getX() / Config.WORLD_TILE_SIZE ),
            (int) Math.floor( getY() / Config.WORLD_TILE_SIZE )
        );
    }
}
