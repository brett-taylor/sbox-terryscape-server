package com.terryscape;

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
}
