package com.terryscape.event;

import java.lang.reflect.Method;

public class EventInvoke {

    private final EventListener eventListener;

    private final Method method;

    public EventInvoke(EventListener eventListener, Method method) {
        this.eventListener = eventListener;
        this.method = method;
    }

    public EventListener getEventListener() {
        return eventListener;
    }

    public Method getMethod() {
        return method;
    }
}
