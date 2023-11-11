package com.terryscape;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.CacheLoaderImpl;
import com.terryscape.entity.EntityManagerImpl;
import com.terryscape.event.EventSystemImpl;
import com.terryscape.net.PacketManagerImpl;
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

    private final EntityManagerImpl entityManager;

    private final CacheLoaderImpl cacheLoader;

    @Inject
    public ServerImpl(EventSystemImpl eventSystem, PacketManagerImpl packetManager, EntityManagerImpl entityManager, CacheLoaderImpl cacheLoader) {
        this.eventSystem = eventSystem;
        this.packetManager = packetManager;
        this.entityManager = entityManager;
        this.cacheLoader = cacheLoader;
    }

    @Override
    public void start() {
        long startTime = System.currentTimeMillis();
        LOGGER.info("Starting {}...", Config.NAME);

        eventSystem.start();

        cacheLoader.loadCache();

        packetManager.start();
        
        long endTime = System.currentTimeMillis();
        LOGGER.info("{} has successfully started up in {} milliseconds.", Config.NAME, endTime - startTime);

        LOGGER.info("Starting game loop tick...");
        ScheduledExecutorService gameThreadExecutor = Executors.newSingleThreadScheduledExecutor();
        gameThreadExecutor.scheduleAtFixedRate(this::gameThreadTick, 0, Config.TICK_TIME_MS, TimeUnit.MILLISECONDS);
    }

    private void gameThreadTick() {
        try {
            var start = System.currentTimeMillis();

            entityManager.tick();

            var end = System.currentTimeMillis();
            LOGGER.debug("Game tick executed in {} miliseconds.", end - start);
        } catch (Exception e) {
            LOGGER.error("Main game thread exception.", e);
        }
    }
}
