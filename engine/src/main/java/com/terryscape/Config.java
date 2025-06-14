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

    public static final String ITEM_STATS_CACHE_LOCATION = "%s/Item_Stats.json".formatted(CACHE_LOCATION);

    public static final String NPC_STATS_CACHE_LOCATION = "%s/Npc_Stats.json".formatted(CACHE_LOCATION);

    public static final String SOUND_CACHE_LOCATION = "%s/Sounds.json".formatted(CACHE_LOCATION);

    public static final String PROJECTILE_CACHE_LOCATION = "%s/Projectiles.json".formatted(CACHE_LOCATION);

    public static final int WORLD_REGION_SIZE = 30;

    public static final int WORLD_TILE_SIZE = 50;

    public static final int SLOW_TICK_THRESHOLD_MS = 15;

    public static final int SLOW_PATHFIND_THRESHOLD_MS = 5;
}
