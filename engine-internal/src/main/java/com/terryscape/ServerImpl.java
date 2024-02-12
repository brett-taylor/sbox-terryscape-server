package com.terryscape;

import com.google.common.base.Stopwatch;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.CacheLoaderImpl;
import com.terryscape.event.EventSystemImpl;
import com.terryscape.event.type.OnGameStartedSystemEvent;
import com.terryscape.net.PacketManagerImpl;
import com.terryscape.world.WorldClockImpl;
import com.terryscape.world.WorldManagerImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Singleton
public class ServerImpl implements Server {

    private static final Logger LOGGER = LogManager.getLogger(ServerImpl.class);

    private final EventSystemImpl eventSystem;

    private final PacketManagerImpl packetManager;

    private final WorldManagerImpl worldManager;

    private final CacheLoaderImpl cacheLoader;

    private final WorldClockImpl worldClock;

    @Inject
    public ServerImpl(EventSystemImpl eventSystem, PacketManagerImpl packetManager, WorldManagerImpl worldManager, CacheLoaderImpl cacheLoader,
                      WorldClockImpl worldClock) {

        this.eventSystem = eventSystem;
        this.packetManager = packetManager;
        this.worldManager = worldManager;
        this.cacheLoader = cacheLoader;
        this.worldClock = worldClock;
    }

    @Override
    public void start() {
        var stopwatch = Stopwatch.createStarted();
        LOGGER.info("Starting {}...", Config.NAME);

        cacheLoader.loadCache();

        packetManager.start();

        LOGGER.info("{} has successfully started up in {} milliseconds.", Config.NAME, stopwatch.elapsed(TimeUnit.MILLISECONDS));

        LOGGER.info("Starting game loop tick...");
        ScheduledExecutorService gameThreadExecutor = Executors.newSingleThreadScheduledExecutor();
        gameThreadExecutor.scheduleAtFixedRate(this::gameThreadTick, 0, Config.TICK_TIME_MS, TimeUnit.MILLISECONDS);

        eventSystem.invoke(OnGameStartedSystemEvent.class, new OnGameStartedSystemEvent());
    }

    private void gameThreadTick() {
        try {
            var stopwatch = Stopwatch.createStarted();

            worldClock.incrementTickCount();

            worldManager.tick();

            LOGGER.debug("Game tick executed in {} milliseconds.", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        } catch (Exception e) {
            LOGGER.error("Main game thread exception.", e);
        }
    }
}
