package com.terryscape.entity.component;

import com.terryscape.entity.Entity;
import com.terryscape.entity.event.EntityEvent;

import java.util.function.Consumer;

public abstract class BaseEntityComponent implements EntityComponent {

    private final Entity entity;

    public BaseEntityComponent(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    /**
     * Called when the entity has been registered to the world but has not yet spawned.
     * If the entity has already been registered, then it will be called straight away.
     */
    public void onRegister() {
    }

    /**
     * Called when the entity has been spawned into the world.
     * If the entity has already been spawned, then it will be called straight away.
     */
    public void onSpawn() {
    }

    public void tick() {
    }

    protected <T extends EntityEvent> void subscribe(Class<T> event, Consumer<T> method) {
        subscribe(this, event, method);
    }

    protected <T extends EntityEvent> void subscribe(EntityComponent broadcaster, Class<T> event, Consumer<T> method) {
        getEntity().subscribe(broadcaster, event, this, method);
    }

    protected <T extends EntityEvent> void unsubscribe(EntityComponent broadcaster, Class<T> event) {
        getEntity().unsubscribe(broadcaster, event, this);
    }

    protected <T extends EntityEvent> void invoke(T event) {
        getEntity().invoke(event);
    }
}
