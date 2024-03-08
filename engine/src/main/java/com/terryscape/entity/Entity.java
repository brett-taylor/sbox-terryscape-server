package com.terryscape.entity;

import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.entity.component.EntityComponent;
import com.terryscape.entity.event.EntityEvent;

import java.util.Optional;
import java.util.function.Consumer;

public interface Entity {

    EntityIdentifier getIdentifier();

    EntityPrefabType getPrefabType();

    String getPrefabIdentifier();

    void addComponent(BaseEntityComponent component);

    <T extends EntityComponent> boolean hasComponent(Class<T> componentType);

    <T extends EntityComponent> Optional<T> getComponent(Class<T> componentType);

    <T extends EntityComponent> T getComponentOrThrow(Class<T> componentType);

    <T extends EntityEvent> void subscribe(Class<T> eventType, Consumer<T> eventConsumer);

    <T extends EntityEvent> void invoke(Class<T> eventType, T entityEvent);
}
