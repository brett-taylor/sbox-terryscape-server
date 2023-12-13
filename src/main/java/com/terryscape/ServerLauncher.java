package com.terryscape;

import com.google.inject.Guice;
import content.devtools.ContentDevToolsGuiceModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerLauncher {

    private static final Logger LOGGER = LogManager.getLogger(ServerLauncher.class);

    public static void main(String... args) {

        long startTime = System.currentTimeMillis();

        LOGGER.info("Creating Guice Injector...");
        var guice = Guice.createInjector(
            new EngineInternalGuiceModule(),

            new ContentDevToolsGuiceModule()
        );

        long endTime = System.currentTimeMillis();
        LOGGER.info("Guice completed in {} milliseconds.", endTime - startTime);

        guice.getInstance(Server.class).start();
    }
}
