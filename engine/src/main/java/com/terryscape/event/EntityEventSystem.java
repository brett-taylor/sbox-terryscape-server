package com.terryscape.event;

import com.terryscape.entity.Entity;
import com.terryscape.entity.component.EntityComponent;
import com.terryscape.entity.event.EntityEvent;

import java.util.function.Consumer;

public interface EntityEventSystem {
    public <T extends EntityEvent> void subscribe(Entity broadcaster, Class<T> event, EntityComponent subscriber, Consumer<T> method);
    public <T extends EntityEvent> void unsubscribe(Entity broadcaster, Class<T> event, EntityComponent subscriber);
    public <T extends EntityEvent> void invoke(Entity broadcaster, T event);
    public void onComponentDestroy(EntityComponent component);
    public void onEntityDestroy(Entity entity);
}
