package com.terryscape.event;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Set;

@Singleton
public class EventSystemImpl implements EventSystem {

    private static final Logger LOGGER = LogManager.getLogger(EventSystemImpl.class);

    private final Set<EventListener> eventListenersRaw;

    private final Multimap<Type, EventInvoke> eventListeners;

    @Inject
    public EventSystemImpl(Set<EventListener> eventListenersRaw) {
        this.eventListenersRaw = eventListenersRaw;
        this.eventListeners = ArrayListMultimap.create();
    }

    public void start() {
        var start = System.currentTimeMillis();
        eventListenersRaw.forEach(this::registerEventListener);

        LOGGER.info("Registered {} event listeners in {} milliseconds.", eventListeners.size(), System.currentTimeMillis() - start);
    }

    @Override
    public void invoke(Event event) {
        if (!eventListeners.containsKey(event.getClass())) {
            return;
        }

        for (var eventInvoke : eventListeners.get(event.getClass())) {
            try {
                eventInvoke.getMethod().invoke(eventInvoke.getEventListener(), event);
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOGGER.error("Failed to invoke event", e);
            }
        }
    }

    private void registerEventListener(EventListener eventListener) {
        var eventMethods = Arrays.stream(eventListener.getClass().getDeclaredMethods())
            .filter(method -> method.getParameterCount() == 1)
            .filter(method -> Event.class.isAssignableFrom(method.getParameters()[0].getType()))
            .toList();

        for (var eventMethod : eventMethods) {
            eventListeners.put(eventMethod.getParameters()[0].getType(), new EventInvoke(eventListener, eventMethod));
        }
    }

}
