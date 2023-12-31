package com.terryscape;

import com.terryscape.maths.Vector2Int;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Config {

    public static final int PORT = 8080;

    public static final int WEBSOCKET_PING_TIMEOUT_SECONDS = 10;

    public static final Charset CHARSET = StandardCharsets.UTF_8;

    public static final String NAME = "AN_UNNAMED_GAME";

    public static final int TICK_TIME_MS = 500;

    public static final String CACHE_LOCATION = "/cache";

    public static final String ITEM_CACHE_LOCATION = "%s/Items.json".formatted(CACHE_LOCATION);

    public static final String NPC_CACHE_LOCATION = "%s/Npcs.json".formatted(CACHE_LOCATION);

    public static final String WORLD_REGION_CACHE_LOCATION_DIRECTORY = "%s/Regions".formatted(CACHE_LOCATION);

    public static final Vector2Int WORLD_REGION_SIZE = new Vector2Int(10, 10);
}
