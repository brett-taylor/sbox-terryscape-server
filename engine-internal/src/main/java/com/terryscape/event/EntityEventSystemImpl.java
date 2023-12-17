package com.terryscape.event;

import com.terryscape.entity.Entity;
import com.terryscape.entity.component.EntityComponent;
import com.terryscape.entity.event.EntityEvent;
import jakarta.inject.Singleton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.function.Consumer;

@Singleton
public class EntityEventSystemImpl implements EntityEventSystem {
    private static final Logger LOGGER = LogManager.getLogger(EntityEventSystemImpl.class);
    private final HashMap<EntityComponent, HashMap<Entity, HashSet<Class<EntityEvent>>>> subscribers = new HashMap<>();
    private final HashMap<Entity, HashMap<Class<EntityEvent>, HashMap<EntityComponent, Consumer<EntityEvent>>>> events = new HashMap<>();

    private static String getFullyQualifiedName(EntityComponent component) {
        return component.getEntity().getIdentifier() + "'s " + component.getClass().getName();
    }
    public <T extends EntityEvent> void subscribe(Entity broadcaster, Class<T> event2, EntityComponent subscriber, Consumer<T> method) {
        var event = (Class<EntityEvent>) event2; //TODO: Check if this even works

        subscribers.putIfAbsent(subscriber, new HashMap<>());
        var subscribedEvents = subscribers.get(subscriber);

        subscribedEvents.putIfAbsent(broadcaster, new HashSet<>());
        subscribedEvents.get(broadcaster).add(event);

        events.putIfAbsent(broadcaster, new HashMap<>());
        var broadcastersEvents = events.get(broadcaster);

        broadcastersEvents.putIfAbsent(event, new HashMap<>());
        var eventListeners = broadcastersEvents.get(event);

        if(eventListeners.containsKey(subscriber)) {
            String errorMsg = String.format("%s is already subscribed to %s on Event: %s ",
                    getFullyQualifiedName(subscriber),
                    broadcaster.getIdentifier(),
                    event2.getName());
            LOGGER.error(errorMsg);
            return;
        } else {
            eventListeners.put(subscriber, (Consumer<EntityEvent>)method);
        }
    }

    public <T extends EntityEvent> void unsubscribe(Entity broadcaster, Class<T> event, EntityComponent subscriber) {
        if(!subscribers.containsKey(subscriber)){
            var errorMsg = String.format("%s isn't subscribed to anything.",
                    getFullyQualifiedName(subscriber));
            LOGGER.error(errorMsg);
        } else {
            var subscribersEvents = subscribers.get(subscriber);
            if(!subscribersEvents.containsKey(broadcaster)) {
                var errorMsg = String.format("%s isn't subscribed to %s.",
                        getFullyQualifiedName(subscriber),
                        broadcaster.getIdentifier());
                LOGGER.error(errorMsg);
            } else {
                var subscriberEventList = subscribersEvents.get(broadcaster);
                if(!subscriberEventList.contains(event)) {
                    var errorMsg = String.format("%s isn't subscribed to %s's %s event.",
                            getFullyQualifiedName(subscriber),
                            broadcaster.getIdentifier(),
                            event.getName());
                    LOGGER.error(errorMsg);
                }
                subscriberEventList.remove(event);
            }
        }

        if(!events.containsKey(broadcaster)) {
            var errorMsg = String.format("%s isn't broadcasting to anything.",
                    broadcaster.getIdentifier());
            LOGGER.error(errorMsg);
        } else {
            var broadcasterEvents = events.get(broadcaster);
            if(!broadcasterEvents.containsKey(event)) {
                var errorMsg = String.format("%s isn't broadcasting on event %s.",
                        broadcaster.getIdentifier(),
                        event.getName());
                LOGGER.error(errorMsg);
            } else {
                var broadcasterEventSubscribers = broadcasterEvents.get(event);
                if(!broadcasterEventSubscribers.containsKey(subscriber)) {
                    var errorMsg = String.format("%s is broadcasting event %s, but %s isn't subscribed.",
                            broadcaster.getIdentifier(),
                            event.getName(),
                            getFullyQualifiedName(subscriber));
                    LOGGER.error(errorMsg);
                } else {
                    broadcasterEventSubscribers.remove(subscriber);
                }
            }
        }
    }

    public <T extends EntityEvent> void invoke(Entity broadcaster, T event) {
        if(!events.containsKey(broadcaster)) {
            return;
        }
        var broadcasterEvents = events.get(broadcaster);
        var eventClass = event.getClass();
        if(!broadcasterEvents.containsKey(eventClass)) {
            return;
        }
        var eventListeners = broadcasterEvents.get(eventClass);
        for (Consumer<EntityEvent> entry : eventListeners.values()) {
            var consumer = (Consumer<T>)entry;
            consumer.accept(event);
        }
    }

    public void onComponentDestroy(EntityComponent component) {
        boolean removedSubscriber = false;
        if(subscribers.containsKey(component)) {
            var subscribedEvents = subscribers.get(component);
            //TODO: Change this for to EntrySet and check for correctness on all events.
            for(var broadcaster : subscribedEvents.keySet()){
                if(!events.containsKey(broadcaster)) {
                    var errorMsg = String.format("%s should be subscribed to %s, but %s doesn't have any events.",
                            getFullyQualifiedName(component),
                            broadcaster.getIdentifier(),
                            broadcaster.getIdentifier());
                    LOGGER.error(errorMsg);
                } else {
                    var broadcastersEvents = events.get(broadcaster);
                    var broadcasterHasAnEvent = false;
                    for(var event : broadcastersEvents.entrySet()) {
                        var eventSubscribers = event.getValue();
                        broadcasterHasAnEvent |= eventSubscribers.containsKey(component);
                        eventSubscribers.remove(component);
                    }
                    if (!broadcasterHasAnEvent){
                        var errorMsg = String.format("%s should be subscribed to %s, but isn't.",
                                getFullyQualifiedName(component),
                                broadcaster.getIdentifier());
                        LOGGER.error(errorMsg);
                    }
                }
            }
            subscribers.remove(component);
            removedSubscriber = true;
        }

        StringBuilder completionMessage = new StringBuilder();
        completionMessage
                .append(getFullyQualifiedName(component))
                .append(" was removed from the event system.")
                .append(" It was ")
                .append((removedSubscriber) ? "" : "not ")
                .append("subscribed to other events.");
        LOGGER.info(completionMessage);
    }

    @Override
    public void onEntityDestroy(Entity entity) {
        boolean removedBroadcaster = false;
        if(events.containsKey(entity)) {
            var broadcastingEvents = events.get(entity);
            for(var eventBroadcasts : broadcastingEvents.entrySet()){
                for(var subscriber : eventBroadcasts.getValue().keySet()) {
                    if (!subscribers.containsKey(subscriber)) {
                        var errorMsg = String.format("%s should be broadcast to by %s, but isn't in the subscriber list.",
                                getFullyQualifiedName(subscriber),
                                entity.getIdentifier());
                        LOGGER.error(errorMsg);
                    }
                    else {
                        var subscriberListeners = subscribers.get(subscriber);
                        if (!subscriberListeners.containsKey(entity)) {
                            var errorMsg = String.format("%s should be broadcast to by %s, but isn't subscribed.",
                                    getFullyQualifiedName(subscriber),
                                    entity.getIdentifier());
                            LOGGER.error(errorMsg);
                        } else {
                            subscriberListeners.remove(entity);
                        }
                    }
                }
            }
            events.remove(entity);
            removedBroadcaster = true;
        }
        StringBuilder completionMessage = new StringBuilder();
        completionMessage
                .append(entity.getIdentifier())
                .append(" was removed from the event system.")
                .append(" It was ")
                .append((removedBroadcaster) ? "" : "not ")
                .append("broadcasting to other events.");
        LOGGER.info(completionMessage);
    }
}
