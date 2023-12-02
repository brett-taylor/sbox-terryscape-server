package com.terryscape.event;

import com.google.inject.Singleton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Singleton
public class EventSystemImpl implements EventSystem {

    private static final Logger LOGGER = LogManager.getLogger(EventSystemImpl.class);

    private final Map<Type, List<Consumer<? extends SystemEvent>>> consumers = new HashMap<>();

    @Override
    public <T extends SystemEvent> void subscribe(Class<T> eventType, Consumer<T> eventConsumer) {
        if (!consumers.containsKey(eventType)) {
            consumers.put(eventType, new ArrayList<>());
        }

        consumers.get(eventType).add(eventConsumer);
    }

    @Override
    public <T extends SystemEvent> void invoke(Class<T> eventType, T systemEvent) {
        if (!consumers.containsKey(eventType)) {
            LOGGER.error("Attempted to invoke system event %s that has no subscribed listeners".formatted(eventType.getName()));
            return;
        }

        for (Consumer<? extends SystemEvent> consumer : consumers.get(eventType)) {
            var typedConsumer = (Consumer<T>) consumer;
            typedConsumer.accept(systemEvent);
        }
    }

}
