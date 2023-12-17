package com.terryscape.event;

import com.terryscape.entity.component.EntityComponent;
import com.terryscape.entity.event.EntityEvent;

public interface EntityEventSystem {
    public <T extends EntityEvent> void subscribe(EntityComponent broadcaster, Class<T> event, EntityComponent subscriber, String method);
    public <T extends EntityEvent> void unsubscribe(EntityComponent broadcaster, Class<T> event, EntityComponent subscriber, String method);
    public void invoke(EntityComponent broadcaster, EntityEvent event);
    public void onComponentDestroy(EntityComponent component);
}
