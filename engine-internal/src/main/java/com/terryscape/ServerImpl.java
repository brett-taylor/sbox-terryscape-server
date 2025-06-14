package com.terryscape;

import com.google.common.base.Stopwatch;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.event.EventSystemImpl;
import com.terryscape.event.type.OnGameStartedSystemEvent;
import com.terryscape.event.type.OnTickSystemEvent;
import com.terryscape.net.PacketManagerImpl;
import com.terryscape.world.WorldClockImpl;
import com.terryscape.world.WorldManagerImpl;
import org.apache.logging.log4j.Level;
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

    private final WorldClockImpl worldClock;

    @Inject
    public ServerImpl(EventSystemImpl eventSystem,
                      PacketManagerImpl packetManager,
                      WorldManagerImpl worldManager,
                      WorldClockImpl worldClock) {

        this.eventSystem = eventSystem;
        this.packetManager = packetManager;
        this.worldManager = worldManager;
        this.worldClock = worldClock;
    }

    @Override
    public void start() {
        var stopwatch = Stopwatch.createStarted();
        LOGGER.info("Starting {}...", Config.NAME);

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

            packetManager.removeClosedConnections();

            worldManager.tick();

            eventSystem.invoke(OnTickSystemEvent.class, new OnTickSystemEvent());

            var tickTimeMs = stopwatch.elapsed(TimeUnit.MILLISECONDS);
            var level = tickTimeMs > Config.SLOW_TICK_THRESHOLD_MS ? Level.WARN : Level.DEBUG;
            LOGGER.log(level, "Game tick executed in {} milliseconds.", tickTimeMs);
        } catch (Exception e) {
            LOGGER.error("Main game thread exception.", e);
        }
    }
}
