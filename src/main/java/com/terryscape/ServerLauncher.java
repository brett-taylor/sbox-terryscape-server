package com.terryscape;

import com.google.common.base.Stopwatch;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.terryscape.cache.CacheLoaderImpl;
import content.ContentModules;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;

public class ServerLauncher {

    private static final Logger LOGGER = LogManager.getLogger(ServerLauncher.class);

    public static void main(String... args) {
        var cacheGuice = loadCache();
        startServer(cacheGuice);
    }

    private static Injector loadCache() {
        LOGGER.info("Loading Cache...");
        var stopwatch = Stopwatch.createStarted();

        var guice = Guice.createInjector(new CacheGuiceModule());
        var cacheContentModule = guice.getInstance(CacheLoaderImpl.class).loadCache();
        var guiceWithCacheDefinitions = guice.createChildInjector(cacheContentModule);

        LOGGER.info("Cache loading completed in {} milliseconds.", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return guiceWithCacheDefinitions;
    }

    private static void startServer(Injector cacheGuice) {
        LOGGER.info("Creating Guice Injector for Server...");
        var stopwatch = Stopwatch.createStarted();

        var contentModules = new HashSet<AbstractModule>();
        contentModules.add(new EngineInternalGuiceModule());
        contentModules.addAll(ContentModules.getContentModules());

        var serverGuice = cacheGuice.createChildInjector(contentModules);

        LOGGER.info("Guice for Server completed in {} milliseconds.", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        serverGuice.getInstance(Server.class).start();
    }
}
