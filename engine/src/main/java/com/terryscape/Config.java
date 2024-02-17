package com.terryscape;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Config {

    public static final int PORT = 8080;

    public static final int WEBSOCKET_PING_TIMEOUT_SECONDS = 10;

    public static final Charset CHARSET = StandardCharsets.UTF_8;

    public static final String NAME = "TerryScape";

    public static final int TICK_TIME_MS = 500;

    public static final String CACHE_LOCATION = "/cache";

    public static final String ITEM_CACHE_LOCATION = "%s/Items.json".formatted(CACHE_LOCATION);

    public static final String NPC_CACHE_LOCATION = "%s/Npcs.json".formatted(CACHE_LOCATION);

    public static final String OBJECT_CACHE_LOCATION = "%s/Objects.json".formatted(CACHE_LOCATION);

    public static final String WORLD_REGION_CACHE_LOCATION_DIRECTORY = "%s/Regions".formatted(CACHE_LOCATION);

    public static final int WORLD_REGION_SIZE = 30;

    public static final int WORLD_TILE_SIZE = 50;
}
