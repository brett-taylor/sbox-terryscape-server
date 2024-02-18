package com.terryscape;

import com.google.common.base.Stopwatch;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import content.ContentModules;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;

public class ServerLauncher {

    private static final Logger LOGGER = LogManager.getLogger(ServerLauncher.class);

    public static void main(String... args) {
        LOGGER.info("Creating Guice Injector...");
        var stopwatch = Stopwatch.createStarted();

        var contentModules = new HashSet<AbstractModule>();
        contentModules.add(new EngineInternalGuiceModule());
        contentModules.addAll(ContentModules.getContentModules());

        var guice = Guice.createInjector(contentModules);

        LOGGER.info("Guice completed in {} milliseconds.", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        guice.getInstance(Server.class).start();
    }
}
