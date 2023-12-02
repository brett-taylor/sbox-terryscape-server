package com.terryscape.entity;

import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.entity.component.EntityComponent;
import com.terryscape.entity.event.EntityEvent;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public interface Entity {

    EntityIdentifier getIdentifier();

    EntityPrefabType getEntityPrefabType();

    String getEntityPrefabIdentifier();

    void addComponent(BaseEntityComponent component);

    <T extends EntityComponent> Optional<T> getComponent(Class<T> componentType);

    <T extends EntityComponent> T getComponentOrThrow(Class<T> componentType);

    <T extends EntityEvent> void subscribe(Class<T> eventType, Consumer<T> eventConsumer);

    <T extends EntityEvent> void invoke(Class<T> eventType, T entityEvent);
}
