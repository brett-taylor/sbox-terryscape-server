package com.terryscape.game;

import com.terryscape.entity.Entity;
import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.entity.component.EntityComponent;
import com.terryscape.entity.event.EntityEvent;
import com.terryscape.event.EventSystemImpl;

public abstract class BaseEntityComponentImpl extends BaseEntityComponent {
    public BaseEntityComponentImpl(Entity entity) {
        super(entity);
    }

    @Override
    protected <T extends EntityEvent> void subscribe(Class<T> event, String method) {

        subscribe(this, event, method);
    }

    @Override
    protected <T extends EntityEvent> void subscribe(EntityComponent broadcaster, Class<T> event, String method) {
        getEntity().subscribe(broadcaster, event, this, method);
    }

    @Override
    protected <T extends EntityEvent> void unsubscribe(EntityComponent broadcaster, Class<T> event, String method) {
        getEntity().unsubscribe(broadcaster, event, this, method);
    }

    @Override
    protected <T extends EntityEvent> void invoke(T event) {
        getEntity().invoke(event);
    }
}
