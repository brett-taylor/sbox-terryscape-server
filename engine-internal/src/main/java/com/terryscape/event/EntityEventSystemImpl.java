package com.terryscape.event;

import com.terryscape.entity.Entity;
import com.terryscape.entity.component.EntityComponent;
import com.terryscape.entity.event.EntityEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.util.*;

public class EntityEventSystemImpl implements EntityEventSystem {
    private static final Logger LOGGER = LogManager.getLogger(EntityEventSystemImpl.class);
    private final HashMap<EntityComponent, HashMap<Entity, HashSet<Class<EntityEvent>>>> subscribers = new HashMap<>();
    private final HashMap<Entity, HashMap<Class<EntityEvent>, HashMap<EntityComponent, List<Method>>>> events = new HashMap<>();

    private static <T extends EntityEvent> Method getMethod(EntityComponent subscriber, String method, Class<T> event) {
        try {
            var methodImpl = subscriber.getClass().getDeclaredMethod(method, event);
            methodImpl.setAccessible(true);
            return methodImpl;
        } catch (NoSuchMethodException e) {
            String errorMsg = String.format("Method Name: '%s' w/ Event Parameter: '%s' does not exist within class '%s'.",
                    method, event.getName(), subscriber.getClass().getName());
            LOGGER.error(errorMsg);
            return null;
        }
    }
    private static String getFullyQualifiedName(EntityComponent component) {
        return component.getEntity().getIdentifier() + "'s " + component.getClass().getName();
    }
    public <T extends EntityEvent> void subscribe(Entity broadcaster, Class<T> event2, EntityComponent subscriber, String method) {
        var eventMethod = getMethod(subscriber, method, event2);
        var event = (Class<EntityEvent>) event2; //TODO: Check if this even works
        if(eventMethod == null) return;

        subscribers.putIfAbsent(subscriber, new HashMap<>());
        var subscribedEvents = subscribers.get(subscriber);

        subscribedEvents.putIfAbsent(broadcaster, new HashSet<>());
        subscribedEvents.get(broadcaster).add(event);

        events.putIfAbsent(broadcaster, new HashMap<>());
        var broadcastersEvents = events.get(broadcaster);

        broadcastersEvents.putIfAbsent(event, new HashMap<>());
        var eventListeners = broadcastersEvents.get(event);

        eventListeners.putIfAbsent(subscriber, new ArrayList<>());
        var listenerList = eventListeners.get(subscriber);

        if(listenerList.stream().anyMatch((x) -> x.getName().equals(eventMethod.getName()))) {
            String errorMsg = String.format("%s is already subscribed to %s w/ method: %s ",
                    getFullyQualifiedName(subscriber),
                    broadcaster.getIdentifier(),
                    method);
            LOGGER.error(errorMsg);
            return;
        }
        listenerList.add(eventMethod);
    }

    public <T extends EntityEvent> void unsubscribe(Entity broadcaster, Class<T> event, EntityComponent subscriber, String method) {
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
                    var broadcasterEventSubscriberMethods = broadcasterEventSubscribers.get(subscriber);
                    var removed = broadcasterEventSubscriberMethods.removeIf(x -> x.getName().equals(method));
                    if(!removed) {
                        var errorMsg = String.format("%s is broadcasting event %s, %s is subscribed, but not on method %s.",
                                broadcaster.getIdentifier(),
                                event.getName(),
                                getFullyQualifiedName(subscriber),
                                method);
                        LOGGER.error(errorMsg);
                    }
                }
            }
        }
    }

    public void invoke(Entity broadcaster, EntityEvent event) {
        if(!events.containsKey(broadcaster)) {
            return;
        }
        var broadcasterEvents = events.get(broadcaster);
        var eventClass = event.getClass();
        if(!broadcasterEvents.containsKey(eventClass)) {
            return;
        }
        var eventListeners = broadcasterEvents.get(eventClass);
        for (Map.Entry<EntityComponent, List<Method>> entry : eventListeners.entrySet()) {
            var component = entry.getKey();
            var methodList = entry.getValue();
            for(var method : methodList) {
                try {
                    method.invoke(component, event);
                } catch (Exception e) {
                    var errorBuilder = new StringBuilder();
                    errorBuilder.append("Failed to invoke method '")
                            .append(method.getName())
                            .append("' on subscriber '")
                            .append(component.getClass().getName())
                            .append("' on entity '")
                            .append(component.getEntity().getIdentifier())
                            .append("' for event '")
                            .append(event.getClass().getName())
                            .append("' triggered by broadcaster '")
                            .append(broadcaster.getIdentifier())
                            .append("'.");
                    //Failed to invoke method '%s' on subscriber '%s' on entity '%s' for event '%s' triggered by broadcaster '%s' on entity '%s'.
                    LOGGER.error(errorBuilder);
                }
            }
        }
    }

    public void onComponentDestroy(EntityComponent component) {
        boolean removedSubscriber = false;
        boolean removedBroadcaster = false;
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

        var componentBroadcaster = component.getEntity();
        if(events.containsKey(componentBroadcaster)) {
            var broadcastingEvents = events.get(componentBroadcaster);
            for(var eventBroadcasts : broadcastingEvents.entrySet()){
                for(var subscriber : eventBroadcasts.getValue().keySet()) {
                    if (!subscribers.containsKey(subscriber)) {
                        var errorMsg = String.format("%s should be broadcast to by %s, but isn't in the subscriber list.",
                                getFullyQualifiedName(subscriber),
                                componentBroadcaster.getIdentifier());
                        LOGGER.error(errorMsg);
                    }
                    else {
                        if (!subscribers.get(subscriber).containsKey(component)) {
                            var errorMsg = String.format("%s should be broadcast to by %s, but isn't subscribed.",
                                    getFullyQualifiedName(subscriber),
                                    getFullyQualifiedName(component));
                            LOGGER.error(errorMsg);
                        } else {
                            subscribers.get(subscriber).remove(component);
                        }
                    }
                }
            }
            events.remove(componentBroadcaster);
            removedBroadcaster = true;
        }
        StringBuilder completionMessage = new StringBuilder();
        completionMessage
                .append(getFullyQualifiedName(component))
                .append(" was removed from the event system.")
                .append(" It was ")
                .append((removedSubscriber) ? "" : "not ")
                .append("subscribed to and ")
                .append((removedBroadcaster) ? "" : "not ")
                .append("broadcasting.");
        LOGGER.info(completionMessage);
    }
}
