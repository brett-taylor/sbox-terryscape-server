package com.terryscape.event;

import com.terryscape.entity.Entity;
import com.terryscape.entity.component.EntityComponent;
import com.terryscape.entity.event.EntityEvent;

public interface EntityEventSystem {
    public <T extends EntityEvent> void subscribe(Entity broadcaster, Class<T> event, EntityComponent subscriber, String method);
    public <T extends EntityEvent> void unsubscribe(Entity broadcaster, Class<T> event, EntityComponent subscriber, String method);
    public void invoke(Entity broadcaster, EntityEvent event);
    public void onComponentDestroy(EntityComponent component);
    public void onEntityDestroy(Entity entity);
}
