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

    boolean isValid();

    void addComponent(BaseEntityComponent component);

    <T extends EntityComponent> Optional<T> getComponent(Class<T> componentType);

    <T extends EntityComponent> T getComponentOrThrow(Class<T> componentType);

    <T extends EntityEvent> void invoke(T event);
    <T extends EntityEvent> void subscribe(EntityComponent broadcaster, Class<T> event, EntityComponent component, String method);
    <T extends EntityEvent> void unsubscribe(EntityComponent broadcaster, Class<T> event, EntityComponent component, String method);
}
