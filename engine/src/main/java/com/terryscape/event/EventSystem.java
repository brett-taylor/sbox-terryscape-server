package com.terryscape.event;

import java.util.function.Consumer;

public interface EventSystem {

    <T extends SystemEvent> void subscribe(Class<T> eventType, Consumer<T> eventConsumer);

    <T extends SystemEvent> void invoke(Class<T> eventType, T systemEvent);
}
